package alice._native.jni.JNINativeInterface_;

import alice.injector.Shellcode;
import alice.util.ClassUtil;
import alice.util.JNIUtil;
import alice.util.MemoryUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

//    jobject (JNICALL *NewGlobalRef) (JNIEnv *env, jobject lobj);
public class NewGlobalRef {

    private static final long code_base;

    private static long holder() {
        return System.in.hashCode();
    }

    static {
        holder();
        byte[] payload = new byte[32];
        payload[0] = (byte) 0x48;
        payload[1] = (byte) 0xbf;
        //JNIEnv here
        payload[10] = (byte) 0x48;
        payload[11] = (byte) 0xbe;
        //lobj here
        payload[20] = (byte) 0x48;
        payload[21] = (byte) 0xb8;
        //function
        payload[30] = (byte) 0xff;
        payload[31] = (byte) 0xe0;
        code_base = MemoryUtil.allocate(payload.length);
        Unsafe.writeBytes(code_base, payload);
        long JNIEnv = JNIUtil.getJNIEnv();
        long function = Unsafe.getLong(Unsafe.getLong(JNIEnv) + (long) Unsafe.ADDRESS_SIZE * 21);
        Unsafe.putLong(code_base + 22, function);
        InstanceKlass klass = ClassUtil.getKlass(NewGlobalRef.class);
        Method method = klass.findMethod("holder", "()J");
        Shellcode.antiOptimization(method);
        Shellcode.setInterpretedEntry(method, code_base);
    }

    public synchronized static long invoke(long JNIEnv, long lobj) {
        Unsafe.putLong(code_base + 2, JNIEnv);
        Unsafe.putLong(code_base + 12, lobj);
        Shellcode.dump(code_base, 32, System.out);
        return holder();
    }
}
