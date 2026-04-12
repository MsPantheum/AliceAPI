package java.lang;

@SuppressWarnings("all")
public abstract class ClassLoader {
    public static ClassLoader getPlatformClassLoader() {
        return null;
    }

    public static ClassLoader getSystemClassLoader() {
        return null;
    }

    static class NativeLibrary {
        native void load(String name, boolean isBuiltin);

        native long find(String name);
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return null;
    }
}
