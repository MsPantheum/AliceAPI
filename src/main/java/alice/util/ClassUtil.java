package alice.util;

import alice.Platform;
import alice.log.Logger;
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
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassUtil {

    public static boolean isClassFile(byte[] data, int offset) {
        return data.length > 4 && data[offset] == (byte) 0xca && data[offset + 1] == (byte) 0xfe && data[offset + 2] == (byte) 0xba && data[offset + 3] == (byte) 0xbe;
    }

    private static final MethodHandle addURL;

    static {

        if (!Platform.jigsaw) {
            try {
                addURL = ReflectionUtil.findVirtual(URLClassLoader.class, "addURL", MethodType.methodType(void.class, URL.class));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            addURL = null;
        }

    }

    public static void append(String path, ClassLoader classLoader) {
        append(Paths.get(path), classLoader);
    }

    public static void append(Path path, ClassLoader classLoader) {
        Object ucp = ClassLoaderUtil.getUCP(classLoader);
        try {
            URL url = path.toUri().toURL();
            if (Platform.jigsaw) {
                ((jdk.internal.loader.URLClassPath) ucp).addURL(url);
            } else {
                ((URLClassPath) ucp).addURL(url);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] dumpFromMemory(Class<?> clazz) {
        InstanceKlass klass = ClassUtil.getKlass(clazz);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            new ClassWriter(klass, bos).write();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return bos.toByteArray();
    }

    public static byte[] readRawBytes(String name) {
        byte[] data = readRawBytes((URLClassLoader) ClassLoader.getSystemClassLoader(), name);
        if (data == null) {
            name = name.replace('.', '/').concat(".class");
            try {
                data = ((URLClassPath) ClassLoaderUtil.bcp).getResource(name).getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return data;
    }

    public static byte[] readRawBytes(URLClassLoader loader, String name) {
        name = name.replace('.', '/').concat(".class");
        System.out.println(name);
        URLClassPath ucp = (URLClassPath) ClassLoaderUtil.getUCP(loader);
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

    private static final boolean DEBUG_CLASS_ENSURER = "true".equals(System.getProperty("alice.debug.class_init"));

    public static void ensureClassesInJarLoaded(String... jars) {
        for (String path : jars) {
            if (path.equals(FileUtil.ALICE_PATH)) {
                continue;
            }
            Logger.MAIN.debug("Ensure classes in " + path + " is loaded...");
            try (JarFile jar = new JarFile(path)) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.endsWith(".class") && !name.endsWith("module-info.class")) {
                        name = name.substring(0, name.length() - 6).replace('/', '.');
                        try {
                            Unsafe.ensureClassInitialized(Class.forName(name));
                            if (DEBUG_CLASS_ENSURER) {
                                Logger.MAIN.trace(name);
                            }
                        } catch (Throwable ignored) {
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static final MethodHandle defineClass0;
    private static final MethodHandle defineClass1;

    static {
        if (Platform.jigsaw) {
            defineClass0 = ReflectionUtil.findStatic(ClassLoader.class, "defineClass0", MethodType.methodType(Class.class, ClassLoader.class, Class.class, String.class, byte[].class, int.class, int.class, ProtectionDomain.class, boolean.class, int.class, Object.class));
            defineClass1 = ReflectionUtil.findStatic(ClassLoader.class, "defineClass1", MethodType.methodType(Class.class, ClassLoader.class, String.class, byte[].class, int.class, int.class, ProtectionDomain.class, String.class));
        } else {
            defineClass0 = null;
            defineClass1 = null;
        }
    }

    public static Class<?> defineClass0(ClassLoader loader, Class<?> lookup, String name, byte[] b, int off, int len, ProtectionDomain pd, boolean initialize, int flags, Object classData) {
        if (!Platform.jigsaw) {
            throw new IllegalStateException();
        }
        try {
            return (Class<?>) defineClass0.invoke(loader, lookup, name, b, off, len, pd, initialize, flags, classData);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> defineClass1(ClassLoader loader, String name, byte[] b, int off, int len, ProtectionDomain pd, String source) {
        if (!Platform.jigsaw) {
            throw new IllegalStateException();
        }
        System.out.println("Attempt to define class ".concat(name));
        try {
            return (Class<?>) defineClass1.invoke(loader, name, b, off, len, pd, source);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
