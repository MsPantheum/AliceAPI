package alice.util;

import alice.Platform;
import jdk.internal.loader.BuiltinClassLoader;
import jdk.internal.loader.ClassLoaders;
import jdk.internal.loader.URLClassPath;

import java.lang.invoke.VarHandle;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Objects;

public class ClassLoaderUtil {
    public static final Object bcp;
    public static final Object ucp;
    private static final VarHandle URLClassLoader_ucp_handle;
    private static final VarHandle BuiltinClassLoader_ucp_handle;
    public static final long ucp_offset;
    public static final VarHandle moduleToReader_handle;
    public static final ClassLoader BOOT_LOADER;

    static {
        try {
            if (Platform.jigsaw) {
                ucp_offset = -1;
                BuiltinClassLoader_ucp_handle = ReflectionUtil.findVarHandle(BuiltinClassLoader.class, "ucp", URLClassPath.class);
                URLClassLoader_ucp_handle = ReflectionUtil.findVarHandle(URLClassLoader.class, "ucp", URLClassPath.class);
                moduleToReader_handle = ReflectionUtil.findVarHandle(BuiltinClassLoader.class, "moduleToReader", Map.class);
                BOOT_LOADER = (ClassLoader) ReflectionUtil.findStaticVarHandle(ClassLoaders.class, "BOOT_LOADER", Class.forName("jdk.internal.loader.ClassLoaders$BootClassLoader")).get();
                bcp = null;
            } else {
                Field f = ReflectionUtil.getField(URLClassLoader.class, "ucp");
                ucp_offset = Unsafe.objectFieldOffset(f);
                URLClassLoader_ucp_handle = null;
                f = ReflectionUtil.getField(Class.forName("sun.misc.Launcher$BootClassPathHolder"), "bcp");
                bcp = Unsafe.getObject(Unsafe.staticFieldBase(f), Unsafe.staticFieldOffset(f));
                moduleToReader_handle = null;
                BOOT_LOADER = null;
                BuiltinClassLoader_ucp_handle = null;
            }
            ucp = getUCP(ClassLoader.getSystemClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getUCP(ClassLoader loader) {
        if (Platform.jigsaw) {
            if (loader instanceof BuiltinClassLoader) {
                return BuiltinClassLoader_ucp_handle.get((BuiltinClassLoader) loader);
            } else if (loader instanceof URLClassLoader) {
                return URLClassLoader_ucp_handle.get((URLClassLoader) loader);
            } else {
                throw new IllegalStateException("Unknown ClassLoader: " + loader);
            }
        } else {
            return Unsafe.getObject(Objects.requireNonNull(loader), ucp_offset);
        }
    }

    public static void setUCP(ClassLoader loader, Object neo) {
        if (Platform.jigsaw) {
            BuiltinClassLoader_ucp_handle.set((BuiltinClassLoader) loader, (URLClassPath) ucp);
        } else {
            Unsafe.putObject(Objects.requireNonNull(loader), ucp_offset, Objects.requireNonNull(neo));
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<ModuleReference, ModuleReader> getModule2Reader(ClassLoader loader) {
        return (Map<ModuleReference, ModuleReader>) moduleToReader_handle.get((BuiltinClassLoader) loader);
    }

    public static void setModule2Reader(ClassLoader loader, Map<ModuleReference, ModuleReader> m2r) {
        moduleToReader_handle.set(loader, m2r);
    }
}
