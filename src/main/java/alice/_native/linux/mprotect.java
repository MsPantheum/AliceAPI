package alice._native.linux;

//int mprotect (void *__addr, size_t __len, int __prot)

import alice._native.ASMUtil;
import alice.injector.MethodInjector;
import alice.util.ClassUtil;
import alice.util.MemoryUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import static alice._native.ASMUtil.Register.*;

public final class mprotect {

    private static final int TRAMPOLINE_SIZE = 33;
    private static final int ARG0_OFFSET = 2;
    private static final int ARG1_OFFSET = 12;
    private static final int ARG2_OFFSET = 26;
    private static final int SYS_MPROTECT = 10;

    private static native int holder();

    private static final long code_base;

    static {
        byte[] payload = new byte[TRAMPOLINE_SIZE];
        code_base = MemoryUtil.allocate(payload.length);
        int p = 0;
        p = ASMUtil.movabs(payload, p, RDI); // __addr
        p = ASMUtil.movabs(payload, p, RSI); // __len
        p = ASMUtil.movImm32(payload, p, RAX, SYS_MPROTECT); // syscall number
        p = ASMUtil.movImm32(payload, p, RDX, 0); // __prot
        payload[p++] = (byte) 0x0f; // syscall
        payload[p++] = (byte) 0x05;
        ASMUtil.ret(payload, p);
        Unsafe.writeBytes(code_base, payload);
        InstanceKlass klass = ClassUtil.getKlass(mprotect.class);
        Method method = klass.findMethod("holder", "()I");
        MethodInjector.setNativePointer(method, code_base);
    }

    public synchronized static int invoke(long __addr, long __len, int __prot) {
        Unsafe.putLong(code_base + ARG0_OFFSET, __addr);
        Unsafe.putLong(code_base + ARG1_OFFSET, __len);
        Unsafe.putInt(code_base + ARG2_OFFSET, __prot);
        return holder();
    }

}
