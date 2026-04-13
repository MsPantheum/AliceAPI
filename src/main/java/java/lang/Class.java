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

    public native Class<? extends T> getSuperclass();

    public native boolean isInstance(Object o);

    public Class<?> getDeclaringClass() {
        return null;
    }

    public native int getModifiers();

    public native Class<?> getComponentType();
}
