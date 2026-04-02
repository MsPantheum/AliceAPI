package alice.util;

import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Klass;
import sun.jvm.hotspot.oops.Metadata;
import sun.jvm.hotspot.tools.jcore.ClassWriter;
import sun.misc.Resource;
import sun.misc.URLClassPath;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClassUtil {

    public static boolean isClassFile(byte[] data, int offset) {
        return data[offset] == (byte) 0xca && data[offset + 1] == (byte) 0xfe && data[offset + 2] == (byte) 0xba && data[offset + 3] == (byte) 0xbe;
    }

    private static final MethodHandle addURL;

    static {
        try {
            addURL = ReflectionUtil.findVirtual(URLClassLoader.class, "addURL", MethodType.methodType(void.class, URL.class));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }

    public static void append(String path, URLClassLoader classLoader) {
        append(Paths.get(path), classLoader);
    }

    public static void append(Path path, URLClassLoader classLoader) {
        try {
            addURL.invoke(classLoader, path.toUri().toURL());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] dumpFromMemory(Class<?> clazz) {
        InstanceKlass klass = ClassUtil.getKlass(clazz);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ClassWriter cw = new ClassWriter(klass, bos);
        try {
            cw.write();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bos.toByteArray();
    }

    public static byte[] readRawBytes(String name) {
        byte[] data = readRawBytes((URLClassLoader) ClassLoader.getSystemClassLoader(), name);
        if (data == null) {
            name = name.replace('.', '/').concat(".class");
            try {
                data = ClassLoaderUtil.bcp.getResource(name).getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return data;
    }

    public static byte[] readRawBytes(URLClassLoader loader, String name) {
        name = name.replace('.', '/').concat(".class");
        System.out.println(name);
        URLClassPath ucp = ClassLoaderUtil.getUCP(loader);
        Resource resource = ucp.getResource(name);
        if (resource != null) {
            try {
                return resource.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static String getJarPath(Class<?> cls) {
        return cls.getProtectionDomain().getCodeSource().getLocation().getPath().replace("!/" + cls.getName().replace('.', '/') + ".class", "").replace("file:", "");
    }

    public static <T extends Klass> T getKlass(Class<?> clazz) {
        //noinspection unchecked
        return (T) Metadata.instantiateWrapperFor(Converter.toAddress(AddressUtil.getKlassAddress(clazz)));
    }

    public static Class<?> getMainClass() {
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        try {
            return Class.forName(traces[traces.length - 1].getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
