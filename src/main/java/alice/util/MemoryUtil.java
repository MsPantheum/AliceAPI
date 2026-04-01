package alice.util;

import alice.Platform;
import alice._native.linux.mmap;
import alice._native.win32.VirtualAlloc;
import sun.jvm.hotspot.debugger.Address;
import sun.jvm.hotspot.memory.Universe;
import sun.jvm.hotspot.types.Type;

import java.nio.charset.StandardCharsets;

import static alice.HSDB.typeDataBase;
import static alice.util.constants.Constants.*;

public class MemoryUtil {
    public static String readSymbol(long symbolAddress) {
        Type symbolType = typeDataBase.lookupType("Symbol");
        long symbol = Unsafe.getAddress(symbolAddress);
        long body = symbol + symbolType.getField("_body").getOffset();
        int length = Unsafe.getShort(symbol + symbolType.getField("_length").getOffset()) & 0xffff;

        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            b[i] = Unsafe.getByte(body + i);
        }
        return new String(b, StandardCharsets.UTF_8);
    }

    public static long allocate(long size) {
        return Platform.win32 ? VirtualAlloc.invoke(0, size, MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE) : mmap.invoke(0, size, PROT_EXEC | PROT_WRITE | PROT_READ, MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObject(Address address) {
        return (T) Ptr2Obj.getFromPtr(Converter.getAddressValue(address));
    }

    private static final long _narrow_klass_base = Universe.getNarrowKlassBase();
    private static final int _narrow_klass_shift = Universe.getNarrowKlassShift();

    public static long decodeNarrowKlass(int narrow) {
        return ((long) narrow << _narrow_klass_shift) + _narrow_klass_base;
    }

    public static int encodeNarrowKlass(long klass) {
        return (int) (klass - _narrow_klass_base) >> _narrow_klass_shift;
    }

    /*
     * This class was tested under permanent System.gc calls and doesn't seem to crash JVM due to object relocations
     */
    public static class Ptr2Obj {
        private static final long _narrow_oop_base = Universe.getNarrowOopBase();
        private static final int _narrow_oop_shift = Universe.getNarrowOopShift();
        private static final long objFieldOffset;

        static {
            try {
                objFieldOffset = Unsafe.objectFieldOffset(Ptr2Obj.class.getDeclaredField("obj"));
            } catch (NoSuchFieldException e) {
                throw new IllegalStateException("Couldn't obtain obj field of own class");
            }
        }

        private volatile Object obj;

        public static Object getFromPtr2Ptr(long address) {
            if (address == 0) {
                return null;
            }
            Ptr2Obj ptr2Obj = new Ptr2Obj();
            Unsafe.compareAndSwapInt(ptr2Obj, objFieldOffset, 0, (int) ((Unsafe.getLong(address) - _narrow_oop_base) >> _narrow_oop_shift));
            return ptr2Obj.obj;
        }

        public static Object getFromPtr2NarrowPtr(long address) {
            if (address == 0) {
                return null;
            }
            Ptr2Obj ptr2Obj = new Ptr2Obj();
            Unsafe.compareAndSwapInt(ptr2Obj, objFieldOffset, 0, (int) Unsafe.getLong(address));
            return ptr2Obj.obj;
        }

        public static int narrowKlassAddress(long address) {
            return (int) ((address - _narrow_oop_base) >> _narrow_oop_shift);
        }


        public static Object getFromPtr(long address) {
            if (address == 0) {
                return null;
            }
            Ptr2Obj ptr2Obj = new Ptr2Obj();
            Unsafe.compareAndSwapInt(ptr2Obj, objFieldOffset, 0, (int) ((address - _narrow_oop_base) >> _narrow_oop_shift));
            return ptr2Obj.obj;
        }

        public static Object getFromNarrowPtr(long address) {
            if (address == 0) {
                return null;
            }
            Ptr2Obj ptr2Obj = new Ptr2Obj();
            Unsafe.compareAndSwapInt(ptr2Obj, objFieldOffset, 0, (int) address);
            return ptr2Obj.obj;
        }
    }
}
