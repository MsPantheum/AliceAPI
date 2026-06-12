package alice._native.jni.JNIInvokeInterface_;

import alice.Platform;
import alice._native.ASMUtil;
import alice.injector.MethodInjector;
import alice.util.*;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import static alice._native.ASMUtil.Register.*;

//jint (JNICALL *GetEnv)(JavaVM *vm, void **penv, jint version);

public final class GetEnv {

    @SuppressWarnings({"DuplicatedCode"})
    private static native int holder();

    private static final long code_base;

    private static final boolean flag = Platform.abi == Platform.ABI.SYSTEM_V;

    static {
        byte[] payload = new byte[flag ? 37 : 54];

        code_base = MemoryUtil.allocate(payload.length);

        long JavaVM = JNIUtil.getJavaVM();
        AddressUtil.checkNull(JavaVM);
        long GetEnv = Unsafe.getLong(Unsafe.getLong(JavaVM) + Unsafe.ADDRESS_SIZE * 6L);
        AddressUtil.checkNull(GetEnv);
        int p = 0;
        if (flag) {
            p = ASMUtil.movabs(payload, p, RDI); // vm
            p = ASMUtil.movabs(payload, p, RSI); // penv
            p = ASMUtil.movabs(payload, p, RAX); // function
            p = ASMUtil.movImm32(payload, p, RDX, 0); // version
            ASMUtil.jmp(payload, p, RAX);
        } else {
            p = ASMUtil.subRsp(payload, p, 0x28);
            p = ASMUtil.movabs(payload, p, RAX); // function
            p = ASMUtil.movStack(payload, p, 0x20, RAX);
            p = ASMUtil.movabs(payload, p, RCX); // vm
            p = ASMUtil.movabs(payload, p, RDX); // penv
            p = ASMUtil.movImm32(payload, p, R8, 0); // version
            p = ASMUtil.callStack(payload, p, 0x20);
            p = ASMUtil.addRsp(payload, p, 0x28);
            ASMUtil.ret(payload, p);
        }

        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + (flag ? 22 : 6), GetEnv);
        InstanceKlass klass = ClassUtil.getKlass(GetEnv.class);
        Method method = klass.findMethod("holder", "()I");
        MethodInjector.setNativePointer(method, code_base);
    }

    public synchronized static int invoke(long vm, long penv, int version) {
        Unsafe.putLong(code_base + (flag ? 2 : 21), vm);
        Unsafe.putLong(code_base + (flag ? 12 : 31), penv);
        Unsafe.putInt(code_base + (flag ? 31 : 41), version);
        return holder();
    }
}
