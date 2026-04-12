package java.lang;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

@SuppressWarnings("all")
public class Class<T> {
    public Constructor<T> getDeclaredConstructor(Class<?>... parameterTypes)
            throws NoSuchMethodException, SecurityException {
        return null;
    }

    public Module getModule() {
        return null;
    }

    public String getName() {
        return null;
    }

    public boolean desiredAssertionStatus() {
        return true;
    }

    public Field getDeclaredField(String name) throws NoSuchFieldException {
        return null;
    }

    public static Class<?> forName(String name) throws ClassNotFoundException {
        return null;
    }

    public ClassLoader getClassLoader() {
        return null;
    }

    public ProtectionDomain getProtectionDomain() {
        return null;
    }

    public Method getDeclaredMethod(String name, Class<?>... parameterTypes)
            throws NoSuchMethodException, SecurityException {
        return null;
    }

    public Class<? extends T> getSuperclass() {
        return null;
    }

    public boolean isInstance(Object o) {
        return true;
    }

    public Class<?> getDeclaringClass() {
        return null;
    }

    public int getModifiers() {
        return 0;
    }
}
