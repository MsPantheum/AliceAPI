package alice.util;

import sun.misc.URLClassPath;

import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.Objects;

public class ClassLoaderUtil {
    public static final URLClassPath bcp;
    public static final URLClassPath ucp;
    public static final long ucp_offset;

    static {
        try {
            Field f = ReflectionUtil.getField(URLClassLoader.class, "ucp");
            ucp_offset = Unsafe.objectFieldOffset(f);
            f = ReflectionUtil.getField(Class.forName("sun.misc.Launcher$BootClassPathHolder"), "bcp");
            bcp = Unsafe.getObject(Unsafe.staticFieldBase(f), Unsafe.staticFieldOffset(f));
            ucp = getUCP((URLClassLoader) ClassLoader.getSystemClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static URLClassPath getUCP(URLClassLoader loader) {
        return Unsafe.getObject(Objects.requireNonNull(loader), ucp_offset);
    }

    public static void setUCP(URLClassLoader loader, URLClassPath neo) {
        Unsafe.putObject(Objects.requireNonNull(loader), ucp_offset, Objects.requireNonNull(neo));
    }
}
