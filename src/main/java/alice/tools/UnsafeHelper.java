package alice.tools;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeHelper {
    private static final Unsafe unsafe;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T allocateInstance(Class<T> cls)
            throws InstantiationException {
        return (T) unsafe.allocateInstance(cls);
    }

    public static long objectFieldOffset(Field f) {
        return unsafe.objectFieldOffset(f);
    }

    public static long staticFieldOffset(Field f) {
        return unsafe.staticFieldOffset(f);
    }

    public static Object staticFieldBase(Field f) {
        return unsafe.staticFieldBase(f);
    }

    public static void putInt(Object o, long offset, int x) {
        unsafe.putInt(o, offset, x);
    }

    public static void putInt(Object o, Field f, int x) {
        putInt(o, objectFieldOffset(f), x);
    }

    public static void putObject(Object o, long offset, Object x) {
        unsafe.putObject(o, offset, x);
    }

    public static void putObject(Object o, Field f, Object x) {
        putObject(o, objectFieldOffset(f), x);
    }

    public static byte getByte(long address) {
        return unsafe.getByte(address);
    }
}
