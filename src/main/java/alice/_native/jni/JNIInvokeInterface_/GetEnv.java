package alice._native.jni.JNIInvokeInterface_;

import alice.Platform;
import alice.injector.Shellcode;
import alice.util.*;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

//jint (JNICALL *GetEnv)(JavaVM *vm, void **penv, jint version);

public final class GetEnv {

    @SuppressWarnings({"DuplicatedCode"})
    private static int holder() {
        return System.out.hashCode();
    }

    private static final long code_base;

    private static final boolean flag = Platform.abi == Platform.ABI.SYSTEM_V;

    static {
        holder();
        byte[] payload = new byte[flag ? 37 : 54];

        code_base = MemoryUtil.allocate(payload.length);

        long JavaVM = JNIUtil.getJavaVM();
        AddressUtil.checkNull(JavaVM);
        long GetEnv = Unsafe.getLong(Unsafe.getLong(JavaVM) + Unsafe.ADDRESS_SIZE * 6L);
        AddressUtil.checkNull(GetEnv);

        System.out.println("GetEnv is: 0x".concat(Long.toHexString(GetEnv)));

        if (flag) {
            payload[0] = (byte) 0x48;
            payload[1] = (byte) (0xbf);
            //vm here
            payload[10] = (byte) 0x48;
            payload[11] = (byte) (0xbe);
            //penv here
            payload[20] = (byte) 0x48;
            payload[21] = (byte) 0xb8;
            //function
            payload[30] = (byte) 0xba;
            //version here
            payload[35] = (byte) 0xff;
            payload[36] = (byte) 0xe0;
        } else {
            payload[0] = (byte) 0x48;
            payload[1] = (byte) 0x83;
            payload[2] = (byte) 0xec;
            payload[3] = (byte) 0x28;
            payload[4] = (byte) 0x48;
            payload[5] = (byte) 0xb8;
            //function
            payload[14] = (byte) 0x48;
            payload[15] = (byte) 0x89;
            payload[16] = (byte) 0x44;
            payload[17] = (byte) 0x24;
            payload[18] = (byte) 0x20;
            payload[19] = (byte) 0x48;
            payload[20] = (byte) 0xb9;
            //vm here
            payload[29] = (byte) 0x48;
            payload[30] = (byte) 0xba;
            //penv here
            payload[39] = (byte) 0x41;
            payload[40] = (byte) 0xb8;
            //version here
            payload[45] = (byte) 0xff;
            payload[46] = (byte) 0x54;
            payload[47] = (byte) 0x24;
            payload[48] = (byte) 0x20;
            payload[49] = (byte) 0x48;
            payload[50] = (byte) 0x83;
            payload[51] = (byte) 0xc4;
            payload[52] = (byte) 0x28;
            payload[53] = (byte) 0xc3;
        }

        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + (flag ? 22 : 6), GetEnv);
        InstanceKlass klass = ClassUtil.getKlass(GetEnv.class);
        Method method = klass.findMethod("holder", "()I");
        Shellcode.antiOptimization(method);
        Shellcode.setInterpretedEntry(method, code_base);
    }

    public synchronized static int invoke(long vm, long penv, int version) {
        Unsafe.putLong(code_base + (flag ? 2 : 21), vm);
        Unsafe.putLong(code_base + (flag ? 12 : 31), penv);
        Unsafe.putInt(code_base + (flag ? 31 : 41), version);
        return holder();
    }
}
