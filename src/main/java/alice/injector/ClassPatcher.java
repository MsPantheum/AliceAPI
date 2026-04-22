package alice.injector;

import alice.LaunchWrapper;
import alice.Platform;
import alice.api.ClassByteProcessor;
import alice.injector.classloading.*;
import alice.injector.patcher.DebuggerBasePatcher;
import alice.injector.patcher.DebuggerLocalPatcher;
import alice.injector.patcher.LinuxDebuggerLocalWorkerThreadPatcher;
import alice.injector.patcher.UniversalPatcher;
import alice.log.Logger;
import alice.util.*;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import sun.misc.URLClassPath;
import sun.net.www.protocol.jar.Handler;
import sun.net.www.protocol.jar.JarURLConnection;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.lang.reflect.Field;
import java.net.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.BiFunction;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassPatcher implements Opcodes {

    private static final Map<String, byte[]> cachedClasses = new ConcurrentHashMap<>();

    private static final class DumpThread extends Thread {

        private static final class DumpInfo {
            private final String path;
            private final byte[] classBytes;

            private DumpInfo(String path, byte[] classBytes) {
                this.path = path;
                this.classBytes = classBytes;
            }
        }

        private static final ArrayBlockingQueue<DumpInfo> classes = new ArrayBlockingQueue<>(1024);

        private DumpThread() {
        }

        {
            setName("AliceClassDumpThread");
            setDaemon(true);
            setPriority(Thread.MIN_PRIORITY);
            start();
            Runtime.getRuntime().addShutdownHook(new Thread(this::flush));
        }

        @Override
        public void run() {
            while (LaunchWrapper.running) {
                flush();
            }
        }

        private void flush() {
            while (!classes.isEmpty()) {
                DumpInfo classData = classes.poll();
                FileUtil.write(classData.path, classData.classBytes);
            }
        }

        private static void startThread() {
            Logger.MAIN.debug("Start class dump thread.");
            new DumpThread();
        }
    }

    private static final boolean LOG_CLASS = "true".equals(System.getProperty("alice.debug.class_patcher.log_class"));

    private static final boolean DUMP_CLASS = "true".equals(System.getProperty("alice.debug.class_patcher.dump"));

    private static final boolean IMMEDIATELY_DUMP_CLASS = "true".equals(System.getProperty("alice.debug.class_patcher.dump_immediately"));

    private static final Class<?> overrideJarLoader;
    private static final MethodHandle overrideJarLoaderConstructor;

    static {
        try {
            Class<?> target = Platform.jigsaw ? Class.forName("jdk.internal.loader.URLClassPath$JarLoader") : Class.forName("sun.misc.URLClassPath$JarLoader");
            String res = Platform.jigsaw ? "jdk/internal/loader/Resource" : "sun/misc/Resource";
            String res_type = 'L' + res + ';';
            String tmp;
            try {
                ReflectionUtil.findVirtual(target, "getResource", MethodType.methodType(Class.forName(res.replace('/', '.')), String.class, boolean.class));
                tmp = "(Ljava/lang/String;Z)" + res_type;
            } catch (NoSuchMethodError e) {
                tmp = "(Ljava/lang/String;)" + res_type;
            }
            final String target_desc = tmp;
            overrideJarLoader = Overrider.override(target, (method, desc) -> {
                if (method.equals("getResource") && desc.equals(target_desc)) {
                    Logger.MAIN.debug("Overriding " + method + desc + ".");
                    return mv -> {
                        mv.visitFieldInsn(GETSTATIC, target.getName().replace('.', '/') + "Overrides", "resourceProcessor", "Ljava/util/function/BiFunction;");
                        mv.visitInsn(SWAP);
                        mv.visitVarInsn(ALOAD, 1);
                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/function/BiFunction", "apply", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
                        mv.visitTypeInsn(CHECKCAST, res);
                    };
                }
                return null;
            }, cw -> {
                FieldVisitor fv = cw.visitField(ACC_PRIVATE | ACC_STATIC, "resourceProcessor", "Ljava/util/function/BiFunction;", "Ljava/util/function/BiFunction<" + res_type + "Ljava/lang/String;>;", null);
                if (Platform.jigsaw) {
                    fv.visitAnnotation("Ljdk/internal/vm/annotation/Stable;", true);
                }
            });
            if (Platform.jigsaw) {
                ReflectionUtil.findStaticVarHandle(overrideJarLoader, "resourceProcessor", BiFunction.class).set(ResourceWrapper.resourceFunction);
            } else {
                Field f = ReflectionUtil.getField(overrideJarLoader, "resourceProcessor");
                Unsafe.putObject(Unsafe.staticFieldBase(f), Unsafe.staticFieldOffset(f), ResourceWrapper.LegacyResource.legacyResourceFunction);
            }
            overrideJarLoaderConstructor = ReflectionUtil.findConstructor(overrideJarLoader, MethodType.methodType(void.class, target));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (DUMP_CLASS) {
            DumpThread.startThread();
        }
    }

    private static Object wrapJarLoader(Object o) {
        try {
            return overrideJarLoaderConstructor.invoke(o);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final PriorityBlockingQueue<ClassByteProcessor> PROCESSORS = new PriorityBlockingQueue<>(64, Comparator.comparingInt(ClassByteProcessor::priority));

    public static boolean shouldRunTransformers() {
        return !PROCESSORS.isEmpty();
    }

    public static byte[] runTransformers(byte[] data, String name) {
        byte[] _try = _protected.get(name);
        if (_try != null) {
            return _try;
        }
        if (data != null && !ClassUtil.isClassFile(data, 0)) {
            Logger.MAIN.warn("ClassPatcher recieved a non class file: ".concat(name).concat(" ,this is not expected!"));
            return data;
        }
        _try = cachedClasses.get(name);
        if (_try != null) {
            return _try;
        }
        if (LOG_CLASS) {
            Logger.MAIN.trace("Transforming class:" + name);
        }
        PROCESSORS.removeIf(ClassByteProcessor::endOfLife);
        for (ClassByteProcessor processor : PROCESSORS) {
            data = processor.process(data, name);
        }
        if (name != null && data != null) {
            cachedClasses.put(name, data);
        }
        if (DUMP_CLASS && data != null) {
            String path = "AliceClassDump".concat(File.separator).concat(Objects.toString(name));
            if (IMMEDIATELY_DUMP_CLASS) {
                FileUtil.write(path, data);
            } else {
                DumpThread.classes.offer(new DumpThread.DumpInfo(path, data));
            }
        }
        return data;
    }

    private static void replaceUCP(ClassLoader loader) {
        Object ucp = ClassLoaderUtil.getUCP(loader);
        if (ucp == null) {
            return;
        }
        Object wrapper = new UCPWrapper.LegacyURLClassPathWrapper((URLClassPath) ucp);
        Logger.MAIN.info("Replacing URLClassPath of " + loader.getClass().getName() + ".");
        ClassLoaderUtil.setUCP(loader, wrapper);
    }

    public static void replaceJarHandler(Object ucp) {
        Handler old = (Handler) UCPUtil.getJarHandler(ucp);
        UCPUtil.setJarHandler(ucp, new StreamPatcher(old == null ? new Handler() : old));
    }

    private static void replaceModule2Reader(ClassLoader loader) {
        Map<ModuleReference, ModuleReader> old = ClassLoaderUtil.getModule2Reader(loader);
        Logger.MAIN.info("Replacing moduleToReader of " + loader.getClass().getName() + ".");
        ClassLoaderUtil.setModule2Reader(loader, new Module2Reader(old));
    }

    private static class WrappedLoaders extends ArrayList<Object> {

        private WrappedLoaders(Collection<Object> orig) {
            for (Object t : orig) {
                if (UCPUtil.isJarLoader(t)) {
                    Logger.MAIN.trace("Wrap JarLoader:" + t);
                    t = wrapJarLoader(t);
                } else {
                    Logger.MAIN.trace("Ignore Loader:" + t);
                }
                super.add(t);
            }
        }

        private WrappedLoaders() {
        }

        @Override
        public boolean add(Object t) {
            if (UCPUtil.isJarLoader(t)) {
                Logger.MAIN.trace("Wrap JarLoader:" + t);
                t = wrapJarLoader(t);
            }
            return super.add(t);
        }
    }

    public static void load() {

        if (Platform.jigsaw) {
            Unsafe.ensureClassInitialized(jdk.internal.loader.URLClassPath.class);
            Unsafe.ensureClassInitialized(Module2Reader.class);
            Unsafe.ensureClassInitialized(WrappedModuleReader.class);
            Unsafe.ensureClassInitialized(ResourceWrapper.StaticResource.class);
            Unsafe.ensureClassInitialized(CollectionWrapper.StaticResources.class);
        } else {
            Unsafe.ensureClassInitialized(URLClassLoader.class);
            Unsafe.ensureClassInitialized(URLClassPath.class);
            Unsafe.ensureClassInitialized(UCPWrapper.LegacyURLClassPathWrapper.class);
            Unsafe.ensureClassInitialized(ResourceWrapper.LegacyResource.LegacyStaticResource.class);
            Unsafe.ensureClassInitialized(CollectionWrapper.LegacyStaticResources.class);
        }

        Unsafe.ensureClassInitialized(UCPUtil.class);
        Unsafe.ensureClassInitialized(UCPWrapper.StaticURLs.class);
        Unsafe.ensureClassInitialized(ClassByteProcessor.class);
        Unsafe.ensureClassInitialized(DebuggerLocalPatcher.class);
        Unsafe.ensureClassInitialized(LinuxDebuggerLocalWorkerThreadPatcher.class);
        Unsafe.ensureClassInitialized(UniversalPatcher.class);
        Unsafe.ensureClassInitialized(Overrider.class);

        try {
            Unsafe.ensureClassInitialized(Class.forName("alice.injector.patcher.UniversalPatcher$1"));
            Unsafe.ensureClassInitialized(Class.forName("alice.injector.patcher.UniversalPatcher$1$1"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        ClassLoader app = ClassLoader.getSystemClassLoader();
        if (Platform.jigsaw) {
            ClassLoader platform = ClassLoader.getPlatformClassLoader();
            Object ucp = ClassLoaderUtil.getUCP(app);
            Logger.MAIN.info("Replacing resource loaders.");
            replaceLoaders(ucp);
            Logger.MAIN.info("Replacing jar handlers.");
            replaceJarHandler(ucp);
            ucp = ClassLoaderUtil.getUCP(platform);
            if (ucp != null) {
                replaceLoaders(ucp);
                replaceJarHandler(ucp);
            }
            replaceModule2Reader(app);
            replaceModule2Reader(platform);
        } else {
            replaceUCP(app);
        }
        registerProcessor(new ClassByteProcessor() {
            @Override
            public byte[] process(byte[] classBytes, String name) {
                return UniversalPatcher.patch(classBytes, name);
            }
        });
        registerProcessor(new ClassByteProcessor() {

            boolean eol = false;

            @Override
            public byte[] process(byte[] classBytes, String name) {
                if ("sun/jvm/hotspot/debugger/DebuggerBase.class".equals(name)) {
                    eol = true;
                    return DebuggerBasePatcher.transform(classBytes);
                }
                return classBytes;
            }

            @Override
            public boolean endOfLife() {
                return eol;
            }
        });
        registerProcessor(new ClassByteProcessor() {

            boolean eol = false;

            @Override
            public byte[] process(byte[] classBytes, String name) {
                if ("sun/jvm/hotspot/debugger/bsd/BsdDebuggerLocal.class".equals(name) || "sun/jvm/hotspot/debugger/windbg/WindbgDebuggerLocal.class".equals(name) || "sun/jvm/hotspot/debugger/linux/LinuxDebuggerLocal.class".equals(name)) {
                    eol = true;
                    return DebuggerLocalPatcher.patch(classBytes, name);
                }
                return classBytes;
            }

            @Override
            public boolean endOfLife() {
                return eol;
            }
        });
        if (Platform.linux) {
            registerProcessor(new ClassByteProcessor() {

                boolean eol = false;

                @Override
                public byte[] process(byte[] classBytes, String name) {
                    if ("sun/jvm/hotspot/debugger/linux/LinuxDebuggerLocal$LinuxDebuggerLocalWorkerThread.class".equals(name)) {
                        eol = true;
                        return LinuxDebuggerLocalWorkerThreadPatcher.patch(classBytes, name);
                    }
                    return classBytes;
                }

                @Override
                public boolean endOfLife() {
                    return eol;
                }
            });
        }
        Logger.MAIN.info("Necessary processors registered.");
    }

    @SuppressWarnings("unchecked")
    public static void replaceLoaders(Object ucp) {
        ArrayList<?> loaders = UCPUtil.getLoaders(ucp);
        WrappedLoaders neo = loaders != null ? new WrappedLoaders((Collection<Object>) loaders) : new WrappedLoaders();
        Logger.MAIN.info("Replacing resource loaders of " + ucp + ".");
        UCPUtil.setLoaders(ucp, neo);
    }

    public static void registerProcessor(ClassByteProcessor processor) {
        Logger.MAIN.info("Registering processor: " + processor.getClass().getName());
        Logger.MAIN.info("Loaded by class: " + processor.getClass().getClassLoader().getClass().getName());
        synchronized (PROCESSORS) {
            PROCESSORS.add(processor);
        }
    }

    private static final Map<String, byte[]> _protected = new HashMap<>();

    public static void addProtectedJar(String path) {
        try (JarFile jar = new JarFile(path)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class") && !entry.getName().startsWith("java/")) {
                    _protected.put(entry.getName(), IOUtil.getByteArray(jar.getInputStream(entry)));
                }
            }
        } catch (IOException e) {
            DebugUtil.printThrowableFully(e);
        }
    }

    static {
        if (!DebugUtil.isRunningTest()) {
            addProtectedJar(ClassUtil.getJarPath(LaunchWrapper.class));
        }
    }

    private static final boolean DEBUG_URL_CREATION = "true".equals(System.getProperty("alice.debug.url_creation"));

    public static URL create(byte[] data, URL original) throws MalformedURLException {
        if (DEBUG_URL_CREATION) {
            Logger.MAIN.debug("Creating URL from: " + original);
        }
        boolean flag = original != null;
        return new URL(flag ? original.getProtocol() : "AliceClassCache", flag ? original.getHost() : null, flag ? original.getPort() : -1, flag ? original.getFile() : "null", new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u) {
                if (u.getProtocol().equals("jar")) {
                    try {
                        return new java.net.JarURLConnection(u) {
                            @Override
                            public JarFile getJarFile() throws IOException {
                                if (original != null) {
                                    String s = original.toString().substring(4);
                                    if (FileUtil.exists(s)) {
                                        return new JarFile(s.substring(0, s.indexOf("!")));
                                    }
                                }
                                return null;
                            }

                            @Override
                            public void connect() {
                            }

                            @Override
                            public InputStream getInputStream() {
                                return new ByteArrayInputStream(data);
                            }
                        };
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    return new URLConnection(u) {
                        @Override
                        public void connect() {
                        }

                        @Override
                        public InputStream getInputStream() {
                            return new ByteArrayInputStream(data);
                        }
                    };
                }
            }
        });
    }

    public static class StreamPatcher extends URLStreamHandler {

        private final Handler delegate;

        public StreamPatcher(Handler delegate) {
            this.delegate = delegate;
        }

        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            try {
                sun.net.www.protocol.jar.JarURLConnection juc = new JarURLConnection(u, delegate);
                if (shouldRunTransformers()) {
                    String s = u.toString();
                    byte[] data = IOUtil.getByteArray(juc.getInputStream());
                    byte[] finalBytes = ClassUtil.isClassFile(data, 0) ? runTransformers(data, s.substring(s.indexOf("!/") + 2)) : data;
                    return new java.net.JarURLConnection(u) {
                        @Override
                        public JarFile getJarFile() throws IOException {
                            String s = u.toString().substring(4);
                            if (FileUtil.exists(s)) {
                                return new JarFile(s.substring(0, s.indexOf("!")));
                            }
                            return null;
                        }

                        @Override
                        public void connect() {
                        }

                        @Override
                        public InputStream getInputStream() {
                            return new ByteArrayInputStream(finalBytes);
                        }
                    };
                }
                return juc;
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
