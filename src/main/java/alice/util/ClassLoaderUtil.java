package alice.util;

import alice.Platform;
import jdk.internal.loader.BuiltinClassLoader;
import jdk.internal.loader.ClassLoaders;

import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Objects;

public class ClassLoaderUtil {
    public static final Object bcp;
    public static final Object ucp;
    public static final long ucp_offset;
    public static final long moduleToReader_offset;
    public static final ClassLoader BOOT_LOADER;

    static {
        try {
            Field f = ReflectionUtil.getField(Platform.jigsaw ? BuiltinClassLoader.class : URLClassLoader.class, "ucp");
            ucp_offset = Unsafe.objectFieldOffset(f);
            if (!Platform.jigsaw) {
                f = ReflectionUtil.getField(Class.forName("sun.misc.Launcher$BootClassPathHolder"), "bcp");
                bcp = Unsafe.getObject(Unsafe.staticFieldBase(f), Unsafe.staticFieldOffset(f));
            } else {
                bcp = null;
            }
            ucp = getUCP(ClassLoader.getSystemClassLoader());
            if (Platform.jigsaw) {
                moduleToReader_offset = Unsafe.objectFieldOffset(ReflectionUtil.getField(BuiltinClassLoader.class, "moduleToReader"));
                BOOT_LOADER = (ClassLoader) ReflectionUtil.findStaticVarHandle(ClassLoaders.class, "BOOT_LOADER", Class.forName("jdk.internal.loader.ClassLoaders$BootClassLoader")).get();
            } else {
                moduleToReader_offset = -1;
                BOOT_LOADER = null;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getUCP(ClassLoader loader) {
        return Unsafe.getObject(Objects.requireNonNull(loader), ucp_offset);
    }

    public static void setUCP(ClassLoader loader, Object neo) {
        Unsafe.putObject(Objects.requireNonNull(loader), ucp_offset, Objects.requireNonNull(neo));
    }

    public static Map<ModuleReference, ModuleReader> getModule2Reader(ClassLoader loader) {
        return Unsafe.getObject(loader, moduleToReader_offset);
    }

    public static void setModule2Reader(ClassLoader loader, Map<ModuleReference, ModuleReader> m2r) {
        Unsafe.putObject(loader, moduleToReader_offset, m2r);
    }
}
