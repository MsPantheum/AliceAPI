package alice.util;

import alice.Platform;
import alice._native.linux.mmap;
import alice._native.linux.munmap;
import alice._native.win32.VirtualAlloc;
import alice.injector.SymbolLookup;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Metadata;
import sun.jvm.hotspot.oops.Method;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.utilities.MethodArray;

import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static alice.HSDB.typeDataBase;
import static alice.util.constants.Constants.*;

public class AddressUtil {

    public static void checkNull(long address) {
        if (address == 0) {
            throw new NullPointerException();
        }
    }

    public static void checkNull(long... addresses) {
        for (long address : addresses) {
            checkNull(address);
        }
    }

    public static long getObjAddress(Object object) {
        if (object == null) {
            return 0L;
        } else {
            Object[] array = new Object[]{object};
            long baseOffset = Unsafe.arrayBaseOffset(Object[].class);
            int addressSize = Unsafe.ADDRESS_SIZE;
            long location;
            switch (addressSize) {
                case 4:
                    location = Unsafe.getInt(array, baseOffset);
                    break;
                case 8:
                    location = Unsafe.getLong(array, baseOffset);
                    break;
                default:
                    throw new Error("unsupported address size: " + addressSize);
            }

            return location * 8L;
        }
    }

    public static void print(String message, long address, PrintStream out) {
        out.print(message);
        print(address, out);
    }

    public static void println(String message, long address, PrintStream out) {
        out.print(message);
        println(address, out);
    }

    public static void print(long address, PrintStream out){
        out.print("0x" + Long.toHexString(address));
    }

    public static void print(long address){
        print(address,System.out);
    }

    public static void print(String message, long address) {
        print(message, address, System.out);
    }

    public static void println(String message, long address) {
        println(message, address, System.out);
    }

    public static void println(long address, PrintStream out){
        out.println("0x" + Long.toHexString(address));
    }

    public static void println(long address){
        println(address,System.out);
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
            long p = Platform.win32 ? VirtualAlloc.invoke(try_addr,size,MEM_COMMIT | MEM_RESERVE,PAGE_EXECUTE_READWRITE) : mmap.invoke(try_addr, size, PROT_READ | PROT_WRITE | PROT_EXEC,
                    MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
            if (p == MAP_FAILED)
                continue;
            if (inRel32Range(target, p))
                return p;
            munmap.invoke(p, size);
        }

        for (try_addr = (target & page_mask) + page_size;
             try_addr <= hi; try_addr += page_size) {
            long p = Platform.win32 ? VirtualAlloc.invoke(try_addr,size,MEM_COMMIT | MEM_RESERVE,PAGE_EXECUTE_READWRITE) : mmap.invoke(try_addr, size, PROT_READ | PROT_WRITE | PROT_EXEC,
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

    public static final long klass_offset;
    public static final long oopSize;

    static {
        oopSize = VM.getVM().getOopSize();
        klass_offset = typeDataBase.lookupType("java_lang_Class").getCIntegerField("_klass_offset").getValue();
    }

    public static long getKlassAddress(Class<?> cls) {
        return oopSize == 8
                ? Unsafe.getLong(cls, klass_offset)
                : Unsafe.getInt(cls, klass_offset) & 0xffffffffL;
    }

    public static long getMethod(MethodInfo methodInfo) {
        InstanceKlass klass = ClassUtil.getKlass(methodInfo.holder);
        @SuppressWarnings("unchecked") List<Method> methods = klass.getImmediateMethods();
        for (Method method : methods) {
            if(method.getName().asString().equals(methodInfo.methodName) && method.getSignature().asString().equals(methodInfo.methodDesc)){
                return Converter.getAddressValue(method.getAddress());
            }
        }
        return 0;
    }

    private static final long method_dataFieldOffset;

    static {
        method_dataFieldOffset = typeDataBase.lookupType("Array<Method*>").getField("_data").getOffset();
    }

    public static long getPointer2Method(MethodInfo methodInfo) {
        InstanceKlass klass = ClassUtil.getKlass(methodInfo.holder);
        MethodArray methods = klass.getMethods();
        long start = Converter.getAddressValue(methods.getAddress());
        long offset = methods.getElemType().getSize();
        for(int i = 0; i < methods.length(); i++) {
            long p = start + method_dataFieldOffset + i * offset;
            long m = Unsafe.getAddress(start + method_dataFieldOffset + i * offset);
            Method method = (Method) Metadata.instantiateWrapperFor(Converter.toAddress(m));
            if(method.getName().asString().equals(methodInfo.methodName) && method.getSignature().asString().equals(methodInfo.methodDesc)){
                return p;
            }
        }
        return 0;
    }

    public static boolean safeAddress(String lib, long address) {
        lib = SymbolLookup.toAbsoluteLibPath(lib);
        LinkedList<ProcReader.MemoryMapping> mappings = ProcReader.parseProcMaps().get(lib);
        for (ProcReader.MemoryMapping mapping : mappings) {
            System.out.println(mapping);
            if (address > Long.parseLong(mapping.addressRangeStart, 16) && address < Long.parseLong(mapping.addressRangeEnd, 16)) {
                return true;
            }
        }
        return false;
    }

    public static boolean safeAddress(long address) {
        Collection<LinkedList<ProcReader.MemoryMapping>> list = ProcReader.parseProcMaps().values();
        for (LinkedList<ProcReader.MemoryMapping> mappings : list) {
            for (ProcReader.MemoryMapping mapping : mappings) {
                if (address > Long.parseLong(mapping.addressRangeStart, 16) && address < Long.parseLong(mapping.addressRangeEnd, 16)) {
                    return true;
                }
            }
        }
        return false;
    }

}
