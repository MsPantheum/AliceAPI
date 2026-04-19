package alice._native.jni.JNIInvokeInterface_;

import alice.injector.Shellcode;
import alice.util.ClassUtil;
import alice.util.JNIUtil;
import alice.util.MemoryUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

//jint (JNICALL *GetEnv)(JavaVM *vm, void **penv, jint version);

public final class GetEnv {

    @SuppressWarnings({"DuplicatedCode"})
    private static int holder() {
        return System.out.hashCode();
    }

    private static final long code_base;

    static {
        holder();

        byte[] payload = new byte[37];

        code_base = MemoryUtil.allocate(payload.length);

        long JavaVM = JNIUtil.getJavaVM();
        long GetEnv = Unsafe.getLong(Unsafe.getLong(JavaVM) + Unsafe.ADDRESS_SIZE * 6L);

        payload[0] = (byte) 0x48;
        payload[1] = (byte) 0xbf;
        //vm here
        payload[10] = (byte) 0x48;
        payload[11] = (byte) 0xbe;
        //penv here
        payload[20] = (byte) 0x48;
        payload[21] = (byte) 0xb8;
        //function
        payload[30] = (byte) 0xba;
        //version here
        payload[35] = (byte) 0xff;
        payload[36] = (byte) 0xe0;

        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 22, GetEnv);
        InstanceKlass klass = ClassUtil.getKlass(GetEnv.class);
        Method method = klass.findMethod("holder", "()I");
        Shellcode.antiOptimization(method);
        Shellcode.setInterpretedEntry(method, code_base);
    }

    public synchronized static int invoke(long vm, long penv, int version) {
        Unsafe.putLong(code_base + 2, vm);
        Unsafe.putLong(code_base + 12, penv);
        Unsafe.putInt(code_base + 31, version);
        Shellcode.dump(code_base, 37, System.out);
        return holder();
    }
}
