package alice._native.jni;

import alice.Platform;
import alice._native.ASMUtil;
import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;
import alice.util.AddressUtil;
import alice.util.ClassUtil;
import alice.util.MemoryUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import static alice._native.ASMUtil.Register.*;

//JNI_GetCreatedJavaVMs(JavaVM **, jsize, jsize *);

public final class JNI_GetCreatedJavaVMs {

    private static native int holder();

    private static final long code_base;

    static {
        byte[] payload = new byte[37];
        boolean flag = Platform.abi == Platform.ABI.SYSTEM_V;
        int p = 0;
        p = ASMUtil.movabs(payload, p, flag ? RDI : RCX); // vmBuf
        p = ASMUtil.movabs(payload, p, flag ? RDX : R8); // nVMs
        p = ASMUtil.movabs(payload, p, RAX); // function
        p = ASMUtil.movImm32(payload, p, flag ? RSI : RDX, 0); // bufLen
        ASMUtil.jmp(payload, p, RAX);
        code_base = MemoryUtil.allocate(payload.length);
        AddressUtil.checkNull(code_base);
        Unsafe.writeBytes(code_base, payload);
        long function = SymbolLookup.lookup("JNI_GetCreatedJavaVMs");
        AddressUtil.checkNull(function);
        Unsafe.putLong(code_base + 22, function);
        InstanceKlass klass = ClassUtil.getKlass(JNI_GetCreatedJavaVMs.class);
        Method method = klass.findMethod("holder", "()I");
        MethodInjector.setNativePointer(method, code_base);
    }

    public synchronized static int invoke(long vmBuf, int bufLen, long nVMs) {
        Unsafe.putLong(code_base + 2, vmBuf);
        Unsafe.putInt(code_base + 31, bufLen);
        Unsafe.putLong(code_base + 12, nVMs);
        return holder();
    }
}
