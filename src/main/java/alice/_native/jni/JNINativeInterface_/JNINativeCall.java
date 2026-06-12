package alice._native.jni.JNINativeInterface_;

import alice.Platform;
import alice._native.ASMUtil;
import alice.injector.MethodInjector;
import alice.util.*;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import static alice._native.ASMUtil.Register.*;

final class JNINativeCall {

    static final int EXCEPTION_OCCURRED = 15;
    static final int EXCEPTION_CLEAR = 17;
    static final int NEW_GLOBAL_REF = 21;
    static final int DELETE_GLOBAL_REF = 22;
    static final int DELETE_LOCAL_REF = 23;
    static final int NEW_LOCAL_REF = 25;
    static final int FIND_CLASS = 6;
    static final int GET_METHOD_ID = 33;
    static final int CALL_OBJECT_METHOD_A = 36;
    static final int CALL_BOOLEAN_METHOD_A = 39;
    static final int CALL_BYTE_METHOD_A = 42;
    static final int CALL_CHAR_METHOD_A = 45;
    static final int CALL_SHORT_METHOD_A = 48;
    static final int CALL_INT_METHOD_A = 51;
    static final int CALL_LONG_METHOD_A = 54;
    static final int CALL_FLOAT_METHOD_A = 57;
    static final int CALL_DOUBLE_METHOD_A = 60;
    static final int CALL_VOID_METHOD_A = 63;
    static final int GET_OBJECT_CLASS = 31;
    static final int GET_STATIC_METHOD_ID = 113;
    static final int CALL_STATIC_OBJECT_METHOD_A = 116;
    static final int CALL_STATIC_BOOLEAN_METHOD_A = 119;
    static final int CALL_STATIC_BYTE_METHOD_A = 122;
    static final int CALL_STATIC_CHAR_METHOD_A = 125;
    static final int CALL_STATIC_SHORT_METHOD_A = 128;
    static final int CALL_STATIC_INT_METHOD_A = 131;
    static final int CALL_STATIC_LONG_METHOD_A = 134;
    static final int CALL_STATIC_FLOAT_METHOD_A = 137;
    static final int CALL_STATIC_DOUBLE_METHOD_A = 140;
    static final int CALL_STATIC_VOID_METHOD_A = 143;
    static final int NEW_STRING_UTF = 167;
    static final int GET_STRING_UTF_CHARS = 169;
    static final int RELEASE_STRING_UTF_CHARS = 170;

    private static final boolean SYSTEM_V = Platform.abi == Platform.ABI.SYSTEM_V;

    private JNINativeCall() {
    }

    static long create(Class<?> holderClass, String holderDescriptor, int functionIndex, int extraArgumentCount) {
        if (extraArgumentCount < 0 || extraArgumentCount > 3) {
            throw new IllegalArgumentException("Unsupported JNI argument count: " + extraArgumentCount);
        }

        byte[] payload = SYSTEM_V ? createSystemV(extraArgumentCount) : createWindowsX64(extraArgumentCount);
        long codeBase = MemoryUtil.allocate(payload.length);
        AddressUtil.checkNull(codeBase);
        Unsafe.writeBytes(codeBase, payload);

        long env = JNIUtil.getJNIEnv();
        long function = Unsafe.getLong(Unsafe.getLong(env) + (long) Unsafe.ADDRESS_SIZE * functionIndex);
        AddressUtil.checkNull(function);
        Unsafe.putLong(codeBase + functionOffset(extraArgumentCount), function);

        InstanceKlass klass = ClassUtil.getKlass(holderClass);
        Method method = klass.findMethod("holder", holderDescriptor);
        MethodInjector.setNativePointer(method, codeBase);
        return codeBase;
    }

    static void setEnv(long codeBase, long env) {
        Unsafe.putLong(codeBase + envOffset(), env);
    }

    static void setArg(long codeBase, int index, long value) {
        Unsafe.putLong(codeBase + argOffset(index), value);
    }

    static void setArg(long codeBase, int index, int value) {
        Unsafe.putLong(codeBase + argOffset(index), value);
    }

    private static byte[] createSystemV(int extraArgumentCount) {
        byte[] payload = new byte[(extraArgumentCount + 2) * 10 + 2];
        int p = 0;
        p = ASMUtil.movabs(payload, p, RDI); // JNIEnv*
        if (extraArgumentCount > 0) {
            p = ASMUtil.movabs(payload, p, RSI);
        }
        if (extraArgumentCount > 1) {
            p = ASMUtil.movabs(payload, p, RDX);
        }
        if (extraArgumentCount > 2) {
            p = ASMUtil.movabs(payload, p, RCX);
        }
        p = ASMUtil.movabs(payload, p, RAX); // function
        ASMUtil.jmp(payload, p, RAX);
        return payload;
    }

    private static byte[] createWindowsX64(int extraArgumentCount) {
        byte[] payload = new byte[(extraArgumentCount + 2) * 10 + 12];
        int p = 0;
        p = ASMUtil.subRsp(payload, p, 0x28);
        p = ASMUtil.movabs(payload, p, RCX); // JNIEnv*
        if (extraArgumentCount > 0) {
            p = ASMUtil.movabs(payload, p, RDX);
        }
        if (extraArgumentCount > 1) {
            p = ASMUtil.movabs(payload, p, R8);
        }
        if (extraArgumentCount > 2) {
            p = ASMUtil.movabs(payload, p, R9);
        }
        p = ASMUtil.movabs(payload, p, RAX); // function
        p = ASMUtil.call(payload, p, RAX);
        p = ASMUtil.addRsp(payload, p, 0x28);
        ASMUtil.ret(payload, p);
        return payload;
    }

    private static long envOffset() {
        return SYSTEM_V ? 2 : 6;
    }

    private static long argOffset(int index) {
        if (index < 0 || index > 2) {
            throw new IllegalArgumentException("Unsupported JNI argument index: " + index);
        }
        return (SYSTEM_V ? 12 : 16) + (long) index * 10L;
    }

    private static long functionOffset(int extraArgumentCount) {
        return (SYSTEM_V ? 2 : 6) + (long) (extraArgumentCount + 1) * 10L;
    }
}
