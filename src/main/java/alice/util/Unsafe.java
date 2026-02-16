package alice.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Unsafe {
    private static final sun.misc.Unsafe UNSAFE;

    static {
        try {
            Constructor<sun.misc.Unsafe> constructor = sun.misc.Unsafe.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            UNSAFE = constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static final int ADDRESS_SIZE = addressSize();
    public static final int PAGE_SIZE = pageSize();

    public static <T> T allocateInstance(Class<T> cls){
        try {
            return (T) UNSAFE.allocateInstance(cls);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static long staticFieldOffset(Field f){
        return UNSAFE.staticFieldOffset(f);
    }

    public static long objectFieldOffset(Field f){
        return UNSAFE.objectFieldOffset(f);
    }

    public static Object staticFieldBase(Field f){
        return UNSAFE.staticFieldBase(f);
    }

    public static Object getObjectVolatile(Object o,long offset){
        return UNSAFE.getObjectVolatile(o, offset);
    }

    public static Object getObject(Object o,long offset){
        return UNSAFE.getObject(o, offset);
    }

    public static int getIntVolatile(Object o,long offset){
        return UNSAFE.getIntVolatile(o, offset);
    }

    public static int getInt(Object o,long offset){
        return UNSAFE.getInt(o, offset);
    }

    public static int getInt(long address){
        return UNSAFE.getInt(address);
    }

    public static long getLongVolatile(Object o,long offset){
        return UNSAFE.getLongVolatile(o, offset);
    }

    public static long getLong(Object o,long offset){
        return UNSAFE.getLong(o, offset);
    }

    public static long getLong(long address){
        return UNSAFE.getLong(address);
    }

    public static void putLong(long address,long value){
        UNSAFE.putLong(address, value);
    }

    public static void putObjectVolatile(Object o, long offset, Object x){
        UNSAFE.putObjectVolatile(o, offset, x);
    }

    public static void putObject(Object o, long offset, Object x){
        UNSAFE.putObject(o, offset, x);
    }

    public static void putLongVolatile(Object o, long offset, long x){
        UNSAFE.putLongVolatile(o, offset, x);
    }

    public static void putLong(Object o, long offset, long x){
        UNSAFE.putLong(o, offset, x);
    }

    public static void putIntVolatile(Object o, long offset, int x){
        UNSAFE.putIntVolatile(o, offset, x);
    }

    public static void putInt(Object o, long offset, int x){
        UNSAFE.putInt(o, offset, x);
    }

    public static int pageSize(){
        return UNSAFE.pageSize();
    }

    public static int addressSize(){
        return UNSAFE.addressSize();
    }

    public static long getAddress(long address){
        return UNSAFE.getAddress(address);
    }

    public static void putAddress(long address, long value){
        UNSAFE.putAddress(address, value);
    }

    public static short getShort(long address){
        return UNSAFE.getShort(address);
    }

    public static void putShort(long address, short value){
        UNSAFE.putShort(address, value);
    }

    public static long getAddress(Object obj){
        Object[] array = new Object[]{obj};


        long baseOffset = UNSAFE.arrayBaseOffset(Object[].class);

        int addressSize = UNSAFE.addressSize();

        long location;

        switch (addressSize) {

            case 4:

                location = UNSAFE.getInt(array, baseOffset);

                break;

            case 8:

                location = UNSAFE.getLong(array, baseOffset);

                break;

            default:

                throw new Error("unsupported address size: " + addressSize);

        }

        return (location) * 8L;
    }
}
