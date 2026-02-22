package alice.injector.patch;

import alice.api.ClassByteProcessor;
import alice.util.URLClassPathWrapper;
import alice.util.Unsafe;
import sun.misc.URLClassPath;

import java.lang.reflect.Field;
import java.net.URLClassLoader;

public class PatcherLoader {

    private static final long ucp_offset;

    static {
        try {
            Field f = URLClassLoader.class.getDeclaredField("ucp");
            ucp_offset = Unsafe.objectFieldOffset(f);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static URLClassPath get(URLClassLoader loader){
        return Unsafe.getObject(loader,ucp_offset);
    }

    private static void set(URLClassLoader loader,Object neo){
        Unsafe.putObject(loader,ucp_offset,neo);
    }

    public static void load(){
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        assert classLoader instanceof URLClassLoader;
        URLClassLoader loader = (URLClassLoader)classLoader;
        URLClassPath ucp = get(loader);
        URLClassPathWrapper wrapper = new URLClassPathWrapper(ucp);
        set(loader,wrapper);
        URLClassPathWrapper.registerProcessor(new ClassByteProcessor() {
            @Override
            public byte[] process(String name, byte[] classBytes) {
                switch (name) {
                    case "sun/jvm/hotspot/debugger/windbg/WindbgDebuggerLocal.class":
                    case "sun/jvm/hotspot/debugger/linux/LinuxDebuggerLocal.class": {
                        return DebuggerLocalPatcher.patch(classBytes,name);
                    }
                    default: {
                        return ClassByteProcessor.super.process(name, classBytes);
                    }

                }
            }
        });
    }
}
