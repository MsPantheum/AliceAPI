package alice.util;

import alice.Platform;
import alice.exception.BadEnvironment;
import alice.exception.ExitNow;
import alice.log.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.ProtectionDomain;

/**
 * Wrapper of Unsafe class. No longer necessary to obtain an Unsafe instance next time!
 */
public final class Unsafe {

    private static boolean INTERNAL_UNSAFE_AVAILABLE = false;

    public static void enableInternalUnsafe() {
        INTERNAL_UNSAFE_AVAILABLE = true;
        Logger.MAIN.info("Switching to InternalUnsafe.");
    }

    private static final class LegacyUnsafe {
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

        public static void putFloatVolatile(Object o, long offset, float x) {
            UNSAFE.putFloatVolatile(o, offset, x);
        }

        public static void putDoubleVolatile(Object o, long offset, double x) {
            UNSAFE.putDoubleVolatile(o, offset, x);
        }

        public static int arrayBaseOffset(Class<?> ac) {
            return UNSAFE.arrayBaseOffset(ac);
        }

        public static int arrayIndexScale(Class<?> ac) {
            return UNSAFE.arrayIndexScale(ac);
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

        public static Class<?> defineClass(String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) {
            return UNSAFE.defineClass(name, b, off, len, loader, protectionDomain);
        }

        public static Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches) {
            return UNSAFE.defineAnonymousClass(hostClass, data, cpPatches);
        }

        public static boolean compareAndSwapObject(Object o, long offset, Object expected, Object x) {
            return UNSAFE.compareAndSwapObject(o, offset, expected, x);
        }

        public static boolean compareAndSwapInt(Object o, long offset, int expected, int x) {
            return UNSAFE.compareAndSwapInt(o, offset, expected, x);
        }

        public static boolean compareAndSwapLong(Object o, long offset, long expected, long x) {
            return UNSAFE.compareAndSwapLong(o, offset, expected, x);
        }
    }

    private static final class InternalUnsafe {
        private static final jdk.internal.misc.Unsafe UNSAFE;

        static {
            UNSAFE = (jdk.internal.misc.Unsafe) ReflectionUtil.findStaticVarHandle(jdk.internal.misc.Unsafe.class, "theUnsafe", jdk.internal.misc.Unsafe.class).get();

        }

        @SuppressWarnings("unchecked")
        public static <T> T allocateInstance(Class<?> cls) throws InstantiationException {
            return (T) UNSAFE.allocateInstance(cls);
        }

        public static void ensureClassInitialized(Class<?> c) {
            UNSAFE.ensureClassInitialized(c);
        }

        public static boolean shouldBeInitialized(Class<?> c) {
            return UNSAFE.shouldBeInitialized(c);
        }

        public static long staticFieldOffset(Field f) {
            return UNSAFE.staticFieldOffset(f);
        }

        public static Object staticFieldBase(Field f) {
            return UNSAFE.staticFieldBase(f);
        }

        public static long arrayBaseOffset(Class<?> arrayClass) {
            return UNSAFE.arrayBaseOffset(arrayClass);
        }

        public static int arrayIndexScale(Class<?> arrayClass) {
            return UNSAFE.arrayIndexScale(arrayClass);
        }

        @SuppressWarnings("unchecked")
        public static <T> T getObjectVolatile(Object o, long offset) {
            return (T) UNSAFE.getReferenceVolatile(o, offset);
        }

        @SuppressWarnings("unchecked")
        public static <T> T getObject(Object o, long offset) {
            return (T) UNSAFE.getReference(o, offset);
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

        public static long allocateMemory(long bytes) {
            return UNSAFE.allocateMemory(bytes);
        }

        public static long reallocateMemory(long address, long bytes) {
            return UNSAFE.reallocateMemory(address, bytes);
        }

        public static void freeMemory(long address) {
            UNSAFE.freeMemory(address);
        }

        @SuppressWarnings("unchecked")
        public static <T> T getUncompressedObject(long address) {
            return (T) UNSAFE.getUncompressedObject(address);
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

        public static void putByteVolatile(Object o, long offset, byte x) {
            UNSAFE.putByteVolatile(o, offset, x);
        }

        public static void putByte(Object o, long offset, byte x) {
            UNSAFE.putByte(o, offset, x);
        }

        public static void putShortVolatile(Object o, long offset, short x) {
            UNSAFE.putShortVolatile(o, offset, x);
        }

        public static void putShort(Object o, long offset, short x) {
            UNSAFE.putShort(o, offset, x);
        }

        public static void putCharVolatile(Object o, long offset, char x) {
            UNSAFE.putCharVolatile(o, offset, x);
        }

        public static void putChar(Object o, long offset, char x) {
            UNSAFE.putChar(o, offset, x);
        }

        public static void putBooleanVolatile(Object o, long offset, boolean x) {
            UNSAFE.putBooleanVolatile(o, offset, x);
        }

        public static void putBoolean(Object o, long offset, boolean x) {
            UNSAFE.putBoolean(o, offset, x);
        }

        public static int getInt(Object o, long offset) {
            return UNSAFE.getInt(o, offset);
        }

        public static long getLongVolatile(Object o, long offset) {
            return UNSAFE.getLongVolatile(o, offset);
        }

        public static long getLong(Object o, long offset) {
            return UNSAFE.getLong(o, offset);
        }

        public static void putLong(Object o, long offset, long x) {
            UNSAFE.putLong(o, offset, x);
        }

        public static void putLongVolatile(Object o, long offset, long x) {
            UNSAFE.putLongVolatile(o, offset, x);
        }

        public static void putInt(Object o, long offset, int x) {
            UNSAFE.putInt(o, offset, x);
        }

        public static void putReferenceVolatile(Object o, long offset, Object x) {
            UNSAFE.putReferenceVolatile(o, offset, x);
        }

        public static void putReference(Object o, long offset, Object x) {
            UNSAFE.putReference(o, offset, x);
        }

        public static void putIntVolatile(Object o, long offset, int x) {
            UNSAFE.putIntVolatile(o, offset, x);
        }

        public static void putFloatVolatile(Object o, long offset, float x) {
            UNSAFE.putFloatVolatile(o, offset, x);
        }

        public static void putDoubleVolatile(Object o, long offset, double x) {
            UNSAFE.putDoubleVolatile(o, offset, x);
        }

        public static int pageSize() {
            return UNSAFE.pageSize();
        }

        public static int addressSize() {
            return UNSAFE.addressSize();
        }

        public static long objectFieldOffset(Field f) {
            return UNSAFE.objectFieldOffset(f);
        }

        public static long objectFieldOffset(Class<?> c, String name) {
            return UNSAFE.objectFieldOffset(c, name);
        }
    }

    public static final int ADDRESS_SIZE = addressSize();
    public static final int PAGE_SIZE = pageSize();

    static {
        if (ADDRESS_SIZE == 4) {
            throw new ExitNow("32bit isn't supported!");
        } else if (ADDRESS_SIZE != 8) {
            throw new ExitNow(new BadEnvironment("Unsupported address size: " + ADDRESS_SIZE));
        }
    }

    public static <T> T allocateInstance(Class<T> cls) {
        try {
            return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.allocateInstance(cls) : LegacyUnsafe.allocateInstance(cls);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void ensureClassInitialized(Class<?> cls) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.ensureClassInitialized(cls);
        } else {
            LegacyUnsafe.ensureClassInitialized(cls);
        }
    }

    public static boolean shouldBeInitialized(Class<?> cls) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.shouldBeInitialized(cls) : LegacyUnsafe.shouldBeInitialized(cls);
    }

    public static long staticFieldOffset(Field f) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.staticFieldOffset(f) : LegacyUnsafe.staticFieldOffset(f);
    }

    public static long objectFieldOffset(Field f) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.objectFieldOffset(f) : LegacyUnsafe.objectFieldOffset(f);
    }

    public static Object staticFieldBase(Field f) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.staticFieldBase(f) : LegacyUnsafe.staticFieldBase(f);
    }

    public static <T> T getObjectVolatile(Object o, long offset) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getObjectVolatile(o, offset) : LegacyUnsafe.getObjectVolatile(o, offset);
    }

    public static <T> T getObject(Object o, long offset) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getObject(o, offset) : LegacyUnsafe.getObject(o, offset);
    }

    public static int getIntVolatile(Object o, long offset) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getIntVolatile(o, offset) : LegacyUnsafe.getIntVolatile(o, offset);
    }

    public static boolean getBooleanVolatile(Object o, long offset) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getBooleanVolatile(o, offset) : LegacyUnsafe.getBooleanVolatile(o, offset);
    }

    public static boolean getBoolean(Object o, long offset) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getBoolean(o, offset) : LegacyUnsafe.getBoolean(o, offset);
    }

    public static byte getByteVolatile(Object o, long offset) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getByteVolatile(o, offset) : LegacyUnsafe.getByteVolatile(o, offset);
    }

    public static byte getByte(Object o, long offset) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getByte(o, offset) : LegacyUnsafe.getByte(o, offset);
    }

    public static short getShortVolatile(Object o, long offset) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getShortVolatile(o, offset) : LegacyUnsafe.getShortVolatile(o, offset);
    }

    public static short getShort(Object o, long offset) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getShort(o, offset) : LegacyUnsafe.getShort(o, offset);
    }

    public static char getCharVolatile(Object o, long offset) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getCharVolatile(o, offset) : LegacyUnsafe.getCharVolatile(o, offset);
    }

    public static char getChar(Object o, long offset) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getChar(o, offset) : LegacyUnsafe.getChar(o, offset);
    }

    public static void putByteVolatile(Object o, long offset, byte b) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putByteVolatile(o, offset, b);
        } else {
            LegacyUnsafe.putByteVolatile(o, offset, b);
        }
    }

    public static void putByte(Object o, long offset, byte b) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putByte(o, offset, b);
        } else {
            LegacyUnsafe.putByte(o, offset, b);
        }
    }

    public static void putShortVolatile(Object o, long offset, short s) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putShortVolatile(o, offset, s);
        } else {
            LegacyUnsafe.putShortVolatile(o, offset, s);
        }
    }

    public static void putShort(Object o, long offset, short s) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putShort(o, offset, s);
        } else {
            LegacyUnsafe.putShort(o, offset, s);
        }
    }

    public static void putCharVolatile(Object o, long offset, char c) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putCharVolatile(o, offset, c);
        } else {
            LegacyUnsafe.putCharVolatile(o, offset, c);
        }
    }

    public static void putChar(Object o, long offset, char c) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putChar(o, offset, c);
        } else {
            LegacyUnsafe.putChar(o, offset, c);
        }
    }

    public static void putBooleanVolatile(Object o, long offset, boolean b) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putBooleanVolatile(o, offset, b);
        } else {
            LegacyUnsafe.putBooleanVolatile(o, offset, b);

        }
    }

    public static void putBoolean(Object o, long offset, boolean b) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putBoolean(o, offset, b);
        } else {
            LegacyUnsafe.putBoolean(o, offset, b);
        }
    }

    public static int getInt(Object o, long offset) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getInt(o, offset) : LegacyUnsafe.getInt(o, offset);
    }

    public static int getInt(long address) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getInt(null, address) : LegacyUnsafe.getInt(address);
    }

    public static long getLongVolatile(Object o, long offset) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getLongVolatile(o, offset) : LegacyUnsafe.getLongVolatile(o, offset);
    }

    public static long getLong(Object o, long offset) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getLong(o, offset) : LegacyUnsafe.getLong(o, offset);
    }

    public static long getLong(long address) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getLong(null, address) : LegacyUnsafe.getLong(address);
    }

    public static void putLong(long address, long value) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putLong(null, address, value);
        } else {
            LegacyUnsafe.putLong(address, value);
        }
    }

    public static void putInt(long address, int value) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putInt(null, address, value);
        } else {
            LegacyUnsafe.putInt(address, value);
        }
    }

    public static void putObjectVolatile(Object o, long offset, Object x) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putReferenceVolatile(o, offset, x);
        } else {
            LegacyUnsafe.putObjectVolatile(o, offset, x);
        }
    }

    public static void putObject(Object o, long offset, Object x) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putReference(o, offset, x);
        } else {
            LegacyUnsafe.putObject(o, offset, x);
        }
    }

    public static void putLongVolatile(Object o, long offset, long x) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putLongVolatile(o, offset, x);
        } else {
            LegacyUnsafe.putLongVolatile(o, offset, x);
        }
    }

    public static void putLong(Object o, long offset, long x) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putLong(o, offset, x);
        } else {
            LegacyUnsafe.putLong(o, offset, x);
        }
    }

    public static void putIntVolatile(Object o, long offset, int x) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putIntVolatile(o, offset, x);
        } else {
            LegacyUnsafe.putIntVolatile(o, offset, x);
        }
    }

    public static void putFloatVolatile(Object o, long offset, float x) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putFloatVolatile(o, offset, x);
        } else {
            LegacyUnsafe.putFloatVolatile(o, offset, x);
        }
    }

    public static void putDoubleVolatile(Object o, long offset, double x) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putDoubleVolatile(o, offset, x);
        } else {
            LegacyUnsafe.putDoubleVolatile(o, offset, x);
        }
    }

    public static long arrayBaseOffset(Class<?> ac) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.arrayBaseOffset(ac) : LegacyUnsafe.arrayBaseOffset(ac);
    }

    public static int arrayIndexScale(Class<?> ac) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.arrayIndexScale(ac) : LegacyUnsafe.arrayIndexScale(ac);
    }

    public static void putInt(Object o, long offset, int x) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putInt(o, offset, x);
        } else {
            LegacyUnsafe.putInt(o, offset, x);
        }
    }

    public static int pageSize() {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.pageSize() : LegacyUnsafe.pageSize();
    }

    public static int addressSize() {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.addressSize() : LegacyUnsafe.addressSize();
    }

    public static long getAddress(long address) {
        return getLong(address);
    }

    public static void putAddress(long address, long value) {
        putLong(address, value);
    }

    public static short getShort(long address) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getShort(null, address) : LegacyUnsafe.getShort(address);
    }

    public static void putShort(long address, short value) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putShort(null, address, value);
        } else {
            LegacyUnsafe.putShort(address, value);
        }
    }

    public static byte getByte(long address) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.getByte(null, address) : LegacyUnsafe.getByte(address);
    }

    public static void putByte(long address, byte value) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.putByte(null, address, value);
        } else {
            LegacyUnsafe.putByte(address, value);
        }
    }

    public static long getAddress(Object obj) {
        Object[] array = new Object[]{obj};


        long baseOffset = arrayBaseOffset(Object[].class);

        long location;

        switch (ADDRESS_SIZE) {

            case 4:

                location = getInt(array, baseOffset);

                break;

            case 8:

                location = getLong(array, baseOffset);

                break;

            default:

                throw new Error("unsupported address size: " + ADDRESS_SIZE);

        }

        return (location) * 8L;
    }

    public static long allocateMemory(long bytes) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.allocateMemory(bytes) : LegacyUnsafe.allocateMemory(bytes);
    }

    public static long reallocateMemory(long address, long bytes) {
        return INTERNAL_UNSAFE_AVAILABLE ? InternalUnsafe.reallocateMemory(address, bytes) : LegacyUnsafe.reallocateMemory(address, bytes);
    }

    public static void freeMemory(long address) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            InternalUnsafe.freeMemory(address);
        } else {
            LegacyUnsafe.freeMemory(address);
        }
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

    public static void copy(long source, long target, long numBytes) {
        for (long i = 0; i < numBytes; i++) {
            putByte(target + i, getByte(source + i));
        }
    }

    public static Class<?> defineClass(String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) {
        if (Platform.jigsaw) {
            return ClassUtil.defineClass1(loader, name, b, off, len, protectionDomain, "Alice");
        } else {
            return LegacyUnsafe.defineClass(name, b, off, len, loader, protectionDomain);
        }
    }

    public static Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches) {
        if (Platform.jigsaw) {
            throw new IllegalStateException();
        } else {
            return LegacyUnsafe.defineAnonymousClass(hostClass, data, cpPatches);
        }
    }

    public static boolean compareAndSwapObject(Object o, long offset, Object expected, Object x) {
        if (Platform.jigsaw) {
            throw new IllegalStateException();
        }
        return LegacyUnsafe.compareAndSwapObject(o, offset, expected, x);
    }

    public static boolean compareAndSwapInt(Object o, long offset, int expected, int x) {
        if (Platform.jigsaw) {
            throw new IllegalStateException();
        }
        return LegacyUnsafe.compareAndSwapInt(o, offset, expected, x);
    }

    public static boolean compareAndSwapLong(Object o, long offset, long expected, long x) {
        if (Platform.jigsaw) {
            throw new IllegalStateException();
        }
        return LegacyUnsafe.compareAndSwapLong(o, offset, expected, x);
    }

    public static <T> T getUncompressedObject(long address) {
        if (Platform.jigsaw && INTERNAL_UNSAFE_AVAILABLE) {
            return InternalUnsafe.getUncompressedObject(address);
        }
        throw new IllegalStateException();
    }

    public static long objectFieldOffset(Class<?> c, String name) {
        if (INTERNAL_UNSAFE_AVAILABLE) {
            return InternalUnsafe.objectFieldOffset(c, name);
        } else {
            return LegacyUnsafe.objectFieldOffset(ReflectionUtil.getField(c, name));
        }
    }
}
