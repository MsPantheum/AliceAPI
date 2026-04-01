package alice.injector.patch;

import alice.api.ClassByteProcessor;
import alice.util.ClassLoaderUtil;
import alice.util.URLClassPathWrapper;
import alice.util.Unsafe;
import sun.misc.URLClassPath;

import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

public class ClassPatcher {

    private static final List<ClassByteProcessor> PROCESSORS = new LinkedList<>();

    public static boolean shouldRunTransformers(){
        return !PROCESSORS.isEmpty();
    }

    public static byte[] runTransformers(byte[] data,String name){
        for (ClassByteProcessor processor : PROCESSORS) {
            data = processor.process(data,name);
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
            Unsafe.ensureClassInitialized(Class.forName("alice.injector.patch.UniversalPatcher$1"));
            Unsafe.ensureClassInitialized(Class.forName("alice.injector.patch.UniversalPatcher$1$1"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        assert classLoader instanceof URLClassLoader;
        URLClassLoader loader = (URLClassLoader)classLoader;
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
                        return DebuggerLocalPatcher.patch(classBytes,name);
                    }
                    case "sun/jvm/hotspot/debugger/linux/LinuxDebuggerLocal$LinuxDebuggerLocalWorkerThread.class":{
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
        PROCESSORS.add(processor);
    }
}
