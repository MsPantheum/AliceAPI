package alice._native.linux;

import alice._native.ASMUtil;
import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;
import alice.util.ClassUtil;
import alice.util.MemoryUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import static alice._native.ASMUtil.Register.*;

//int munmap (void *__addr, size_t __len)

public final class munmap {

    private static native int holder();

    private static final long code_base;

    static {
        byte[] payload = new byte[32];
        int p = 0;
        p = ASMUtil.movabs(payload, p, RDI); // __addr
        p = ASMUtil.movabs(payload, p, RSI); // __len
        p = ASMUtil.movabs(payload, p, RAX); // function
        ASMUtil.jmp(payload, p, RAX);
        code_base = MemoryUtil.allocate(32);
        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 22, SymbolLookup.lookup("munmap"));
        InstanceKlass klass = ClassUtil.getKlass(munmap.class);
        Method method = klass.findMethod("holder", "()I");
        MethodInjector.setNativePointer(method, code_base);
    }

    public synchronized static int invoke(long __addr, long __len) {
        Unsafe.putLong(code_base + 2, __addr);
        Unsafe.putLong(code_base + 12, __len);
        return holder();
    }

}
