package alice.util;

import alice.HSDB;
import alice.Init;
import alice.Platform;
import alice._native.VirtualAlloc;
import alice._native.mmap;
import alice._native.munmap;
import sun.jvm.hotspot.debugger.Address;
import sun.jvm.hotspot.debugger.bsd.BsdDebuggerLocal;
import sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal;
import sun.jvm.hotspot.debugger.windbg.WindbgDebuggerLocal;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Metadata;
import sun.jvm.hotspot.oops.Method;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.types.Type;
import sun.jvm.hotspot.utilities.MethodArray;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static alice.HSDB.typeDataBase;
import static alice.util.Constants.*;

public class AddressUtil {
    public static long getAddressValue(Address addr) {
        return HSDB.debugger.getAddressValue(addr);
    }

    public static void print(long address, PrintStream out){
        out.print("0x" + Long.toHexString(address));
    }

    public static void print(long address){
        print(address,System.out);
    }

    public static void println(long address, PrintStream out){
        out.println("0x" + Long.toHexString(address));
    }

    public static void println(long address){
        println(address,System.out);
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
        Init.ensureInit();
        oopSize = VM.getVM().getOopSize();
        klass_offset = typeDataBase.lookupType("java_lang_Class").getCIntegerField("_klass_offset").getValue();
    }

    public static long getKlassAddress(Class<?> cls) {
        return oopSize == 8
                ? Unsafe.getLong(cls, klass_offset)
                : Unsafe.getInt(cls, klass_offset) & 0xffffffffL;
    }

    public static long getMethod(MethodInfo methodInfo) {
        long klass_addr = getKlassAddress(methodInfo.holder);
        InstanceKlass klass = (InstanceKlass) Metadata.instantiateWrapperFor(toAddress(klass_addr));
        @SuppressWarnings("unchecked") List<Method> methods = klass.getImmediateMethods();
        for (Method method : methods) {
            if(method.getName().asString().equals(methodInfo.methodName) && method.getSignature().asString().equals(methodInfo.methodDesc)){
                return getAddressValue(method.getAddress());
            }
        }
        return 0;
    }

    private static final long method_dataFieldOffset;

    static {
        method_dataFieldOffset = typeDataBase.lookupType("Array<Method*>").getField("_data").getOffset();
    }

    public static long getPointer2Method(MethodInfo methodInfo) {
        long klass_addr = getKlassAddress(methodInfo.holder);
        InstanceKlass klass = (InstanceKlass) Metadata.instantiateWrapperFor(toAddress(klass_addr));
        MethodArray methods = klass.getMethods();
        long start = getAddressValue(methods.getAddress());
        long offset = methods.getElemType().getSize();
        for(int i = 0; i < methods.length(); i++) {
            long p = start + method_dataFieldOffset + i * offset;
            long m = Unsafe.getAddress(start + method_dataFieldOffset + i * offset);
            Method method = (Method) Metadata.instantiateWrapperFor(toAddress(m));
            if(method.getName().asString().equals(methodInfo.methodName) && method.getSignature().asString().equals(methodInfo.methodDesc)){
                return p;
            }
        }
        return 0;
    }

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
}
