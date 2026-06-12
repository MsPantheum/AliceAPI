package alice._native.jni.JNINativeInterface_;

import alice.Platform;
import alice._native.ASMUtil;
import alice._native.CString;
import alice.injector.MethodInjector;
import alice.util.*;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import static alice._native.ASMUtil.Register.*;

//jclass (JNICALL *FindClass)(JNIEnv *env, const char *name);
public final class FindClass {

    private static final long code_base;

    private static final boolean SYSTEM_V = Platform.abi == Platform.ABI.SYSTEM_V;

    static {
        byte[] payload = SYSTEM_V ? createSystemV() : createWindowsX64();
        code_base = MemoryUtil.allocate(payload.length);
        AddressUtil.checkNull(code_base);
        Unsafe.writeBytes(code_base, payload);

        long env = JNIUtil.getJNIEnv();
        Unsafe.putLong(code_base + envOffset(), env);
        Unsafe.putLong(code_base + findClassOffset(), function(env, JNINativeCall.FIND_CLASS));
        Unsafe.putLong(code_base + secondEnvOffset(), env);
        Unsafe.putLong(code_base + newGlobalRefOffset(), function(env, JNINativeCall.NEW_GLOBAL_REF));

        InstanceKlass klass = ClassUtil.getKlass(FindClass.class);
        Method method = klass.findMethod("holder", "()J");
        MethodInjector.setNativePointer(method, code_base);
    }

    private static native long holder();

    private FindClass() {
    }

    public synchronized static long invoke(long JNIEnv, long name) {
        Unsafe.putLong(code_base + envOffset(), JNIEnv);
        Unsafe.putLong(code_base + argOffset(), name);
        Unsafe.putLong(code_base + secondEnvOffset(), JNIEnv);
        return holder();
    }

    public synchronized static long invoke(long JNIEnv, String name) {
        CString cname = CString.create(name);
        try {
            return invoke(JNIEnv, cname.address);
        } finally {
            cname.release();
        }
    }

    public synchronized static long invoke(String name) {
        return invoke(JNIUtil.getJNIEnv(), name);
    }

    private static long function(long env, int index) {
        long function = Unsafe.getLong(Unsafe.getLong(env) + (long) Unsafe.ADDRESS_SIZE * index);
        AddressUtil.checkNull(function);
        return function;
    }

    private static byte[] createSystemV() {
        byte[] payload = new byte[66];
        int p = 0;
        p = ASMUtil.subRsp(payload, p, 0x08);
        p = ASMUtil.movabs(payload, p, RDI); // JNIEnv*
        p = ASMUtil.movabs(payload, p, RSI); // class name
        p = ASMUtil.movabs(payload, p, RAX); // FindClass
        p = ASMUtil.call(payload, p, RAX);
        p = ASMUtil.movabs(payload, p, RDI); // JNIEnv*
        p = ASMUtil.mov(payload, p, RSI, RAX); // local jclass
        p = ASMUtil.movabs(payload, p, RAX); // NewGlobalRef
        p = ASMUtil.call(payload, p, RAX);
        p = ASMUtil.addRsp(payload, p, 0x08);
        ASMUtil.ret(payload, p);
        return payload;
    }

    private static byte[] createWindowsX64() {
        byte[] payload = new byte[66];
        int p = 0;
        p = ASMUtil.subRsp(payload, p, 0x28);
        p = ASMUtil.movabs(payload, p, RCX); // JNIEnv*
        p = ASMUtil.movabs(payload, p, RDX); // class name
        p = ASMUtil.movabs(payload, p, RAX); // FindClass
        p = ASMUtil.call(payload, p, RAX);
        p = ASMUtil.movabs(payload, p, RCX); // JNIEnv*
        p = ASMUtil.mov(payload, p, RDX, RAX); // local jclass
        p = ASMUtil.movabs(payload, p, RAX); // NewGlobalRef
        p = ASMUtil.call(payload, p, RAX);
        p = ASMUtil.addRsp(payload, p, 0x28);
        ASMUtil.ret(payload, p);
        return payload;
    }

    private static long envOffset() {
        return 6;
    }

    private static long argOffset() {
        return 16;
    }

    private static long findClassOffset() {
        return 26;
    }

    private static long secondEnvOffset() {
        return 38;
    }

    private static long newGlobalRefOffset() {
        return 51;
    }
}
