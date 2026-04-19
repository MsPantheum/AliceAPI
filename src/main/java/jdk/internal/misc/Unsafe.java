package jdk.internal.misc;

import java.lang.reflect.Field;

public class Unsafe {
    private static Unsafe theUnsafe;

    public native Object allocateInstance(Class<?> cls) throws InstantiationException;

    public void ensureClassInitialized(Class<?> c) {
    }

    public boolean shouldBeInitialized(Class<?> c) {
        return false;
    }

    public long staticFieldOffset(Field f) {
        return 0;
    }

    public Object staticFieldBase(Field f) {
        return null;
    }

    public int arrayBaseOffset(Class<?> arrayClass) {
        return 0;
    }

    public native Object getReferenceVolatile(Object o, long offset);

    public native Object getReference(Object o, long offset);

    public native int getIntVolatile(Object o, long offset);

    public native boolean getBooleanVolatile(Object o, long offset);

    public native boolean getBoolean(Object o, long offset);

    public long allocateMemory(long bytes) {
        return 0;
    }

    public long reallocateMemory(long address, long bytes) {
        return 0;
    }

    public void freeMemory(long address) {

    }

    public native Object getUncompressedObject(long address);

    public native byte getByteVolatile(Object o, long offset);

    public native byte getByte(Object o, long offset);

    public native short getShortVolatile(Object o, long offset);

    public native short getShort(Object o, long offset);

    public native char getCharVolatile(Object o, long offset);

    public native char getChar(Object o, long offset);

    public native void putByteVolatile(Object o, long offset, byte x);

    public native void putByte(Object o, long offset, byte x);

    public native void putShortVolatile(Object o, long offset, short x);

    public native void putShort(Object o, long offset, short x);

    public native void putCharVolatile(Object o, long offset, char x);

    public native void putChar(Object o, long offset, char x);

    public native void putBooleanVolatile(Object o, long offset, boolean x);

    public native void putBoolean(Object o, long offset, boolean x);

    public native int getInt(Object o, long offset);

    public native long getLongVolatile(Object o, long offset);

    public native long getLong(Object o, long offset);

    public native void putLong(Object o, long offset, long x);

    public native void putLongVolatile(Object o, long offset, long x);

    public native void putInt(Object o, long offset, int x);

    public native void putReferenceVolatile(Object o, long offset, Object x);

    public native void putReference(Object o, long offset, Object x);

    public native void putIntVolatile(Object o, long offset, int x);

    public native void putFloatVolatile(Object o, long offset, float x);

    public native void putDoubleVolatile(Object o, long offset, double x);

    public int arrayIndexScale(Class<?> arrayClass) {
        return 0;
    }

    public int pageSize() {
        return 0;
    }

    public int addressSize() {
        return 0;
    }

    public long objectFieldOffset(Field f) {
        return 0;
    }

    public long objectFieldOffset(Class<?> c, String name) {
        return 0;
    }
}
