package alice.interceptor;

import alice.injector.ClassPatcher;
import alice.util.Unsafe;

import java.util.HashMap;
import java.util.Map;

public final class ClassLoaderInterceptor {

    private static final ClassLoader loader = ClassLoader.getSystemClassLoader();

    private static final Map<String, Class<?>> cache = new HashMap<>();

    public static Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.startsWith("alice.generated.")) {
            Class<?> ret = cache.get(name);
            if (ret != null) {
                return ret;
            }
            byte[] data = ClassPatcher.runProviders(name.replace(".", "/").concat(".class"));
            if (data != null) {
                ClassLoader current = Thread.currentThread().getContextClassLoader();
                ret = Unsafe.defineClass(name, data, 0, data.length, current, Object.class.getProtectionDomain());
                cache.put(name, ret);
                return ret;
            }
        }
        return loader.loadClass(name);
    }
}
