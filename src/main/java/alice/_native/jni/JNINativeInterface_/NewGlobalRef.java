package alice._native.jni.JNINativeInterface_;

import alice._native.InlineHook;
import alice.injector.Shellcode;
import alice.util.AddressUtil;
import alice.util.JNIUtil;
import alice.util.MemoryUtil;
import alice.util.Unsafe;

//    jobject (JNICALL *NewGlobalRef) (JNIEnv *env, jobject lobj);
public class NewGlobalRef {

    private static final long code_base;

    private static long holder() {
        long lllll = 11221144L;
        int iii = 14514;
        while (iii != 0 && lllll > 0) {
            iii--;
            lllll -= iii;
        }
        return System.in.hashCode();
    }

    static {
        for (int i = 0; i < 20000; i++) {
            holder();
        }
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
        long address = Shellcode.getCompiledEntry(NewGlobalRef.class, "holder", "()J");
        AddressUtil.checkNull(address);
        InlineHook.simpleHook(address, code_base);
    }

    public synchronized static long invoke(long JNIEnv, long lobj) {
        Unsafe.putLong(code_base + 2, JNIEnv);
        Unsafe.putLong(code_base + 12, lobj);
        Shellcode.dump(code_base, 32, System.out);
        return holder();
    }
}
