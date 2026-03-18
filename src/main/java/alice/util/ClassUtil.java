package alice.util;

import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Metadata;
import sun.jvm.hotspot.tools.jcore.ClassWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClassUtil {

    private static final MethodHandle addURL;

    static {
        try {
            addURL = ReflectionUtil.findVirtual(URLClassLoader.class, "addURL", MethodType.methodType(void.class, URL.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void append(String path, URLClassLoader classLoader){
        append(Paths.get(path),classLoader);
    }

    public static void append(Path path, URLClassLoader classLoader){
        try {
            addURL.invoke(classLoader, path.toUri().toURL());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] dump(Class<?> clazz){
        InstanceKlass klass = (InstanceKlass) Metadata.instantiateWrapperFor(AddressUtil.toAddress(AddressUtil.getKlassAddress(clazz)));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ClassWriter cw = new ClassWriter(klass,bos);
        try {
            cw.write();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bos.toByteArray();
    }

    public static String getPath(Class<?> cls){
        return cls.getProtectionDomain().getCodeSource().getLocation().getPath().replace("!/"+cls.getName().replace('.','/')+".class", "").replace("file:", "");
    }
}
