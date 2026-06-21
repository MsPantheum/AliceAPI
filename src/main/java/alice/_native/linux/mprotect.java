package alice._native.linux;

//int mprotect (void *__addr, size_t __len, int __prot)

import alice._native.ASMUtil;
import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;
import alice.util.ClassUtil;
import alice.util.MemoryUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import static alice._native.ASMUtil.Register.*;

public final class mprotect {

    private static native int holder();

    private static final long code_base;

    static {
        byte[] payload = new byte[37];
        code_base = MemoryUtil.allocate(payload.length);
        int p = 0;
        p = ASMUtil.movabs(payload, p, RDI); // __addr
        p = ASMUtil.movabs(payload, p, RSI); // __len
        p = ASMUtil.movabs(payload, p, RAX); // function
        p = ASMUtil.movImm32(payload, p, RDX, 0); // __prot
        ASMUtil.jmp(payload, p, RAX);
        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 22, SymbolLookup.lookup("mprotect"));
        InstanceKlass klass = ClassUtil.getKlass(mprotect.class);
        Method method = klass.findMethod("holder", "()I");
        MethodInjector.setNativePointer(method, code_base);
    }

    public synchronized static int invoke(long __addr, long __len, int __prot) {
        Unsafe.putLong(code_base + 2, __addr);
        Unsafe.putLong(code_base + 12, __len);
        Unsafe.putInt(code_base + 31, __prot);
        return holder();
    }

}
