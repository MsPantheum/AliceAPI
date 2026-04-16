package alice.interceptor;

public final class ClassLoaderInterceptor {

    private static final ClassLoader loader = ClassLoader.getSystemClassLoader();

    public static Class<?> findClass(String name) throws ClassNotFoundException {
        return loader.loadClass(name);
    }
}
