package alice.tools;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public class ReflectionHelper {
    private static final MethodHandles.Lookup LOOKUP;

    static {
        try {
            LOOKUP = UnsafeHelper.allocateInstance(MethodHandles.Lookup.class);
            Field f = MethodHandles.Lookup.class.getDeclaredField("allowedModes");
            UnsafeHelper.putInt(LOOKUP, f, -1);
            f = MethodHandles.Lookup.class.getDeclaredField("lookupClass");
            UnsafeHelper.putObject(LOOKUP, f, Object.class);
        } catch (InstantiationException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle findVirtual(Class<?> owner, String name, MethodType type) throws NoSuchMethodException {
        try {
            return LOOKUP.findVirtual(owner, name, type);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle findVirtual(Class<?> owner, String name, Class<?> ret, Class<?>... params) throws NoSuchMethodException {
        return findVirtual(owner, name, MethodType.methodType(ret, params));
    }

    public static MethodHandle findStatic(Class<?> owner, String name, MethodType type) throws NoSuchMethodException {
        try {
            return LOOKUP.findStatic(owner, name, type);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle findStatic(Class<?> owner, String name, Class<?> ret, Class<?>... params) throws NoSuchMethodException {
        return findStatic(owner, name, MethodType.methodType(ret, params));
    }
}
