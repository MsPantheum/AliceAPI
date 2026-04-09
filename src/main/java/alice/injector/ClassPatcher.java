package alice.injector;

import alice.LaunchWrapper;
import alice.api.ClassByteProcessor;
import alice.injector.patcher.DebuggerLocalPatcher;
import alice.injector.patcher.LinuxDebuggerLocalWorkerThreadPatcher;
import alice.injector.patcher.UniversalPatcher;
import alice.util.*;
import org.apache.commons.io.IOUtils;
import sun.misc.URLClassPath;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassPatcher {

    private static final boolean DUMP_CLASS = "true".equals(System.getProperty("alice.debug.class_patcher.dump"));

    private static final PriorityQueue<ClassByteProcessor> PROCESSORS = new PriorityQueue<>(Comparator.comparingInt(ClassByteProcessor::priority));

    public static boolean shouldRunTransformers() {
        return !PROCESSORS.isEmpty();
    }

    public static byte[] runTransformers(byte[] data, String name) {
        byte[] _try = _protected.get(name);
        if (_try != null) {
            return _try;
        }
        for (ClassByteProcessor processor : PROCESSORS) {
            data = processor.process(data, name);
        }
        if (DUMP_CLASS && data != null) {
            FileUtil.write("AliceClassDump" + File.separator + name, data);
        }
        return data;
    }

    public static void load() {
        Unsafe.ensureClassInitialized(URLClassLoader.class);
        Unsafe.ensureClassInitialized(URLClassPath.class);
        Unsafe.ensureClassInitialized(URLClassPathWrapper.class);
        Unsafe.ensureClassInitialized(URLClassPathWrapper.StaticResource.class);
        Unsafe.ensureClassInitialized(URLClassPathWrapper.StaticResources.class);
        Unsafe.ensureClassInitialized(URLClassPathWrapper.StaticURLs.class);
        Unsafe.ensureClassInitialized(ClassByteProcessor.class);
        Unsafe.ensureClassInitialized(DebuggerLocalPatcher.class);
        Unsafe.ensureClassInitialized(LinuxDebuggerLocalWorkerThreadPatcher.class);
        Unsafe.ensureClassInitialized(UniversalPatcher.class);

        try {
            Unsafe.ensureClassInitialized(Class.forName("alice.injector.patcher.UniversalPatcher$1"));
            Unsafe.ensureClassInitialized(Class.forName("alice.injector.patcher.UniversalPatcher$1$1"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        assert classLoader instanceof URLClassLoader;
        URLClassLoader loader = (URLClassLoader) classLoader;
        URLClassPath ucp = ClassLoaderUtil.getUCP(loader);
        URLClassPathWrapper wrapper = new URLClassPathWrapper(ucp);
        System.out.println("Replacing URLClassPath.");
        ClassLoaderUtil.setUCP(loader, wrapper);
        System.out.println("Replaced.");
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
        System.out.println("Necessary processors registered.");
    }

    public static void registerProcessor(ClassByteProcessor processor) {
        System.out.println("Registering processor: " + processor.getClass().getName());
        System.out.println("Loaded by class: " + processor.getClass().getClassLoader().getClass().getName());
        System.out.println("CP loaded by: " + ClassPatcher.class.getClassLoader().getClass().getName());
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
}
