package alice.interceptor;

import alice.injector.ClassPatcher;
import alice.util.Unsafe;

public final class ClassLoaderInterceptor {

    private static final ClassLoader loader = ClassLoader.getSystemClassLoader();

    public static Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.startsWith("alice.generated.")) {
            System.out.println("Meow!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(name);
            byte[] data = ClassPatcher.runProviders(name.replace(".", "/").concat(".class"));
            if (data != null) {
                ClassLoader current = Thread.currentThread().getContextClassLoader();
                return Unsafe.defineClass(name, data, 0, data.length, current, Object.class.getProtectionDomain());
            }
        }
        return loader.loadClass(name);
    }
}
