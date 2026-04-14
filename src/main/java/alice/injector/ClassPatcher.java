package alice.injector;

import alice.LaunchWrapper;
import alice.Platform;
import alice.api.ClassByteProcessor;
import alice.injector.classloading.*;
import alice.injector.patcher.DebuggerLocalPatcher;
import alice.injector.patcher.LinuxDebuggerLocalWorkerThreadPatcher;
import alice.injector.patcher.UniversalPatcher;
import alice.log.Logger;
import alice.util.*;
import org.apache.commons.io.IOUtils;
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
import java.util.function.BiFunction;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassPatcher implements Opcodes {

    private static final boolean LOG_CLASS = "true".equals(System.getProperty("alice.debug.class_patcher.log_class"));

    private static final boolean DUMP_CLASS = "true".equals(System.getProperty("alice.debug.class_patcher.dump"));

    private static final Class<?> overrideJarLoader;
    private static final MethodHandle overrideJarLoaderConstructor;

    static {
        try {
            Class<?> target = Platform.jigsaw ? Class.forName("jdk.internal.loader.URLClassPath$JarLoader") : Class.forName("sun.misc.URLClassPath$JarLoader");
            String res = Platform.jigsaw ? "jdk/internal/loader/Resource" : "sun/misc/Resource";
            String res_type = 'L' + res + ';';
            overrideJarLoader = Overrider.override(target, (method, desc) -> {
                if (method.equals("getResource") && desc.equals("(Ljava/lang/String;Z)" + res_type)) {
                    return mv -> {
                        mv.visitFieldInsn(GETSTATIC, target.getName().replace('.', '/') + "Overrides", "resourceProcessor", "Ljava/util/function/BiFunction;");
                        mv.visitInsn(SWAP);
                        mv.visitVarInsn(ALOAD, 1);
                        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/function/BiFunction", "apply", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
                        mv.visitTypeInsn(CHECKCAST, res);
                    };
                }
                return null;
            }, cw -> cw.visitField(ACC_PRIVATE | ACC_STATIC, "resourceProcessor", "Ljava/util/function/BiFunction;", "Ljava/util/function/BiFunction<" + res_type + "Ljava/lang/String;>;", null));
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
    }

    private static Object wrapJarLoader(Object o) {
        try {
            return overrideJarLoaderConstructor.invoke(o);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final PriorityQueue<ClassByteProcessor> PROCESSORS = new PriorityQueue<>(Comparator.comparingInt(ClassByteProcessor::priority));

    public static boolean shouldRunTransformers() {
        return !PROCESSORS.isEmpty();
    }

    public static byte[] runTransformers(byte[] data, String name) {
        byte[] _try = _protected.get(name);
        if (_try != null) {
            return _try;
        }
        if (LOG_CLASS) {
            Logger.MAIN.trace("Transforming class:" + name);
        }
        for (ClassByteProcessor processor : PROCESSORS) {
            data = processor.process(data, name);
        }
        if (DUMP_CLASS && data != null) {
            FileUtil.write("AliceClassDump" + File.separator + name, data);
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
            replaceLoaders(ucp);
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
                switch (name) {
                    case "sun/jvm/hotspot/debugger/bsd/BsdDebuggerLocal.class":
                    case "sun/jvm/hotspot/debugger/windbg/WindbgDebuggerLocal.class":
                    case "sun/jvm/hotspot/debugger/linux/LinuxDebuggerLocal.class": {
                        return DebuggerLocalPatcher.patch(classBytes, name);
                    }
                    case "sun/jvm/hotspot/debugger/linux/LinuxDebuggerLocal$LinuxDebuggerLocalWorkerThread.class": {
                        return LinuxDebuggerLocalWorkerThreadPatcher.patch(classBytes, name);
                    }
                    default: {
                        return UniversalPatcher.patch(classBytes, name);
                    }

                }
            }
        });
        Logger.MAIN.info("Necessary processors registered.");
    }

    @SuppressWarnings("unchecked")
    public static void replaceLoaders(Object ucp) {
        ArrayList<?> loaders = UCPUtil.getLoaders(ucp);
        WrappedLoaders neo = loaders != null ? new WrappedLoaders((Collection<Object>) loaders) : new WrappedLoaders();
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
                    _protected.put(entry.getName(), IOUtils.toByteArray(jar.getInputStream(entry)));
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

    public static URL create(byte[] data, URL original) throws MalformedURLException {
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
            sun.net.www.protocol.jar.JarURLConnection juc = new JarURLConnection(u, delegate);
            byte[] data = juc.getInputStream().readAllBytes();
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
}
