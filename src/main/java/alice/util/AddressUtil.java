package alice.util;

import alice.HSDB;
import alice.Init;
import alice.Platform;
import alice._native.mmap;
import alice._native.munmap;
import sun.jvm.hotspot.debugger.Address;
import sun.jvm.hotspot.debugger.bsd.BsdDebuggerLocal;
import sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal;
import sun.jvm.hotspot.debugger.windbg.WindbgDebuggerLocal;
import sun.jvm.hotspot.runtime.VM;

import static alice.HSDB.typeDataBase;
import static alice.util.Constants.*;

public class AddressUtil {
    public static long getAddressValue(Address addr) {
        return HSDB.debugger.getAddressValue(addr);
    }

    public static Address toAddress(long addr) {
        if (Platform.linux) {
            return ((LinuxDebuggerLocal) HSDB.debugger).newAddress(addr);
        } else if (Platform.win32) {
            return ((WindbgDebuggerLocal) HSDB.debugger).newAddress(addr);
        } else if (Platform.bsd || Platform.darwin) {
            return ((BsdDebuggerLocal) HSDB.debugger).newAddress(addr);
        }
        throw new IllegalStateException("Should not reach here");
    }

    public static long align(long address) {
        return address & -Unsafe.PAGE_SIZE;
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
            long p = mmap.invoke(try_addr, size, PROT_READ | PROT_WRITE | PROT_EXEC,
                    MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
            if (p == MAP_FAILED)
                continue;
            if (inRel32Range(target, p))
                return p;
            munmap.invoke(p, size);
        }

        for (try_addr = (target & page_mask) + page_size;
             try_addr <= hi; try_addr += page_size) {
            long p = mmap.invoke(try_addr, size, PROT_READ | PROT_WRITE | PROT_EXEC,
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

    private static final long klass_offset;
    private static final long oopSize;

    static {
        Init.ensureInit();
        oopSize = VM.getVM().getOopSize();
        klass_offset = typeDataBase.lookupType("java_lang_Class").getCIntegerField("_klass_offset").getValue();
    }

    public static long getKlassAddress(Class<?> cls) {
        return oopSize == 8
                ? Unsafe.getLong(cls, klass_offset)
                : Unsafe.getInt(cls, klass_offset) & 0xffffffffL;
    }
}
