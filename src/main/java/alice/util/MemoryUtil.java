package alice.util;

import alice.Platform;
import alice._native.linux.mmap;
import alice._native.linux.munmap;
import alice._native.win32.VirtualAlloc;
import sun.jvm.hotspot.debugger.Address;
import sun.jvm.hotspot.memory.Universe;
import sun.jvm.hotspot.oops.CompressedKlassPointers;
import sun.jvm.hotspot.oops.CompressedOops;
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

    private static final long _narrow_klass_base = Platform.jigsaw ? CompressedKlassPointers.getBase() : Universe.getNarrowKlassBase();
    private static final int _narrow_klass_shift = Platform.jigsaw ? CompressedKlassPointers.getShift() : Universe.getNarrowKlassShift();

    public static long decodeNarrowKlass(int narrow) {
        return ((long) narrow << _narrow_klass_shift) + _narrow_klass_base;
    }

    public static int encodeNarrowKlass(long klass) {
        return (int) (klass - _narrow_klass_base) >> _narrow_klass_shift;
    }

    //From https://github.com/shdwmtr/libsnare.h
    public static long allocateNear(long target, long size) {
        long lo = target > 0x80000000L ? target - 0x80000000L : 0x10000L;
        long hi = target + 0x80000000L;
        if (hi < target) {
            hi = -1;
        }
        long page_size = Unsafe.PAGE_SIZE;
        @SuppressWarnings("PointlessBitwiseExpression") long page_mask = ~(page_size - 1);
        long try_addr;

        for (try_addr = (target & page_mask) - page_size;
             try_addr >= lo; try_addr -= page_size) {
            long p = Platform.win32 ? VirtualAlloc.invoke(try_addr, size, MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE) : mmap.invoke(try_addr, size, PROT_READ | PROT_WRITE | PROT_EXEC,
                    MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
            if (p == MAP_FAILED)
                continue;
            if (inRel32Range(target, p))
                return p;
            munmap.invoke(p, size);
        }

        for (try_addr = (target & page_mask) + page_size;
             try_addr <= hi; try_addr += page_size) {
            long p = Platform.win32 ? VirtualAlloc.invoke(try_addr, size, MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE) : mmap.invoke(try_addr, size, PROT_READ | PROT_WRITE | PROT_EXEC,
                    MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
            if (p == MAP_FAILED)
                continue;
            if (inRel32Range(target, p))
                return p;
            munmap.invoke(p, size);
        }

        return 0;
    }

    public static boolean inRel32Range(long p1, long p2) {
        long diff = p2 - p1;
        return diff >= -0x7FFFFF00L && diff <= 0x7FFFFF00L;
    }

    /*
     * This class was tested under permanent System.gc calls and doesn't seem to crash JVM due to object relocations
     */
    public static class Ptr2Obj {
        private static final long _narrow_oop_base = Platform.jigsaw ? CompressedOops.getBase() : Universe.getNarrowOopBase();
        private static final int _narrow_oop_shift = Platform.jigsaw ? CompressedOops.getShift() : Universe.getNarrowOopShift();
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
