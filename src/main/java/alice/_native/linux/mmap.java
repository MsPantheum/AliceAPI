package alice._native.linux;

import alice._native.ASMUtil;
import alice.exception.NativeException;
import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;
import alice.util.AddressUtil;
import alice.util.ClassUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import static alice._native.ASMUtil.Register.*;
import static alice.util.constants.Constants.*;

//void *mmap (void *__addr, size_t __len, int __prot,int __flags, int __fd, __off_t __offset)

public final class mmap {

    @SuppressWarnings(value = {"DuplicatedCode"})
    private static native long holder();

    private static final long code_base;

    static {
        byte[] payload = new byte[58];
        int p = 0;
        p = ASMUtil.movabs(payload, p, RDI); // __addr
        p = ASMUtil.movabs(payload, p, RSI); // __len
        p = ASMUtil.movabs(payload, p, R9); // __offset
        p = ASMUtil.movabs(payload, p, RAX); // function
        p = ASMUtil.movImm32(payload, p, RDX, 0); // __prot
        p = ASMUtil.movImm32(payload, p, RCX, 0); // __flags
        p = ASMUtil.movImm32(payload, p, R8, 0); // __fd
        ASMUtil.jmp(payload, p, RAX);
        code_base = Unsafe.allocateMemory(payload.length);
        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 32, SymbolLookup.lookup("mmap"));
        int ret = mprotect.invoke(AddressUtil.align_page(code_base), 1, PROT_READ | PROT_WRITE | PROT_EXEC);
        if (ret != 0) {
            throw new NativeException("mmap failed!", ret);
        }
        InstanceKlass klass = ClassUtil.getKlass(mmap.class);
        Method method = klass.findMethod("holder", "()J");
        MethodInjector.setNativePointer(method, code_base);
    }

    public synchronized static long invoke(long __addr, long __len, int __prot, int __flags, int __fd, long __offset) {
        Unsafe.putLong(code_base + 2, __addr);
        Unsafe.putLong(code_base + 12, __len);
        Unsafe.putLong(code_base + 22, __offset);
        Unsafe.putInt(code_base + 41, __prot);
        Unsafe.putInt(code_base + 46, __flags);
        Unsafe.putInt(code_base + 52, __fd);
        return holder();
    }

}
