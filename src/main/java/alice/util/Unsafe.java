package alice.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.ProtectionDomain;

public class Unsafe {
    private static final sun.misc.Unsafe UNSAFE;

    static {
        try {
            Constructor<sun.misc.Unsafe> constructor = sun.misc.Unsafe.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            UNSAFE = constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static final int ADDRESS_SIZE = addressSize();
    public static final int PAGE_SIZE = pageSize();

    @SuppressWarnings("unchecked")
    public static <T> T allocateInstance(Class<T> cls) {
        try {
            return (T) UNSAFE.allocateInstance(cls);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void ensureClassInitialized(Class<?> cls) {
        UNSAFE.ensureClassInitialized(cls);
    }

    public static boolean shouldBeInitialized(Class<?> cls) {
        return UNSAFE.shouldBeInitialized(cls);
    }

    public static long staticFieldOffset(Field f) {
        return UNSAFE.staticFieldOffset(f);
    }

    public static long objectFieldOffset(Field f) {
        return UNSAFE.objectFieldOffset(f);
    }

    public static Object staticFieldBase(Field f) {
        return UNSAFE.staticFieldBase(f);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObjectVolatile(Object o, long offset) {
        return (T) UNSAFE.getObjectVolatile(o, offset);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObject(Object o, long offset) {
        return (T) UNSAFE.getObject(o, offset);
    }

    public static int getIntVolatile(Object o, long offset) {
        return UNSAFE.getIntVolatile(o, offset);
    }

    public static boolean getBooleanVolatile(Object o, long offset) {
        return UNSAFE.getBooleanVolatile(o, offset);
    }

    public static boolean getBoolean(Object o, long offset) {
        return UNSAFE.getBoolean(o, offset);
    }

    public static byte getByteVolatile(Object o, long offset) {
        return UNSAFE.getByteVolatile(o, offset);
    }

    public static byte getByte(Object o, long offset) {
        return UNSAFE.getByte(o, offset);
    }

    public static short getShortVolatile(Object o, long offset) {
        return UNSAFE.getShortVolatile(o, offset);
    }

    public static short getShort(Object o, long offset) {
        return UNSAFE.getShort(o, offset);
    }

    public static char getCharVolatile(Object o, long offset) {
        return UNSAFE.getCharVolatile(o, offset);
    }

    public static char getChar(Object o, long offset) {
        return UNSAFE.getChar(o, offset);
    }

    public static void putByteVolatile(Object o, long offset, byte b) {
        UNSAFE.putByteVolatile(o, offset, b);
    }

    public static void putByte(Object o, long offset, byte b) {
        UNSAFE.putByte(o, offset, b);
    }

    public static void putShortVolatile(Object o, long offset, short s) {
        UNSAFE.putShortVolatile(o, offset, s);
    }

    public static void putShort(Object o, long offset, short s) {
        UNSAFE.putShort(o, offset, s);
    }

    public static void putCharVolatile(Object o, long offset, char c) {
        UNSAFE.putCharVolatile(o, offset, c);
    }

    public static void putChar(Object o, long offset, char c) {
        UNSAFE.putChar(o, offset, c);
    }

    public static void putBooleanVolatile(Object o, long offset, boolean b) {
        UNSAFE.putBooleanVolatile(o, offset, b);
    }

    public static void putBoolean(Object o, long offset, boolean b) {
        UNSAFE.putBoolean(o, offset, b);
    }

    public static int getInt(Object o, long offset) {
        return UNSAFE.getInt(o, offset);
    }

    public static int getInt(long address) {
        return UNSAFE.getInt(address);
    }

    public static long getLongVolatile(Object o, long offset) {
        return UNSAFE.getLongVolatile(o, offset);
    }

    public static long getLong(Object o, long offset) {
        return UNSAFE.getLong(o, offset);
    }

    public static long getLong(long address) {
        return UNSAFE.getLong(address);
    }

    public static void putLong(long address, long value) {
        UNSAFE.putLong(address, value);
    }

    public static void putInt(long address, int value) {
        UNSAFE.putInt(address, value);
    }

    public static void putObjectVolatile(Object o, long offset, Object x) {
        UNSAFE.putObjectVolatile(o, offset, x);
    }

    public static void putObject(Object o, long offset, Object x) {
        UNSAFE.putObject(o, offset, x);
    }

    public static void putLongVolatile(Object o, long offset, long x) {
        UNSAFE.putLongVolatile(o, offset, x);
    }

    public static void putLong(Object o, long offset, long x) {
        UNSAFE.putLong(o, offset, x);
    }

    public static void putIntVolatile(Object o, long offset, int x) {
        UNSAFE.putIntVolatile(o, offset, x);
    }

    public static void putInt(Object o, long offset, int x) {
        UNSAFE.putInt(o, offset, x);
    }

    public static int pageSize() {
        return UNSAFE.pageSize();
    }

    public static int addressSize() {
        return UNSAFE.addressSize();
    }

    public static long getAddress(long address) {
        assert address != 0;
        return UNSAFE.getAddress(address);
    }

    public static void putAddress(long address, long value) {
        assert address != 0;
        UNSAFE.putAddress(address, value);
    }

    public static short getShort(long address) {
        assert address != 0;
        return UNSAFE.getShort(address);
    }

    public static void putShort(long address, short value) {
        assert address != 0;
        UNSAFE.putShort(address, value);
    }

    public static byte getByte(long address) {
        assert address != 0;
        return UNSAFE.getByte(address);
    }

    public static void putByte(long address, byte value) {
        assert address != 0;
        UNSAFE.putByte(address, value);
    }

    public static long getAddress(Object obj) {
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

    public static long allocateMemory(long bytes) {
        return UNSAFE.allocateMemory(bytes);
    }

    public static long reallocateMemory(long address, long bytes) {
        assert address != 0;
        return UNSAFE.reallocateMemory(address, bytes);
    }

    public static void freeMemory(long address) {
        assert address != 0;
        UNSAFE.freeMemory(address);
    }

    public static byte[] readBytes(long address, long numBytes) {
        assert address != 0;
        byte[] bytes = new byte[Math.toIntExact(numBytes)];
        for (int i = 0; i < numBytes; i++) {
            bytes[i] = getByte(address + i);
        }
        return bytes;
    }

    public static void writeBytes(long address, byte[] payload) {
        writeBytes(address, payload, payload.length);
    }

    public static void writeBytes(long address, byte[] payload, long numBytes) {
        for (int i = 0; i < numBytes; i++) {
            putByte(address + i, payload[i]);
        }
    }

    public static void copy(long source,long target,long numBytes){
        for(long i = 0; i < numBytes; i++){
            putByte(target + i,getByte(source + i));
        }
    }

    public static Class<?> defineClass(String name, byte[] b, int off, int len,
                                       ClassLoader loader,
                                       ProtectionDomain protectionDomain) {
        return UNSAFE.defineClass(name, b, off, len, loader, protectionDomain);
    }

    public static Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches) {
        return UNSAFE.defineAnonymousClass(hostClass, data, cpPatches);
    }
}
