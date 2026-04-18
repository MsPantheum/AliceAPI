package alice._native.jni.JavaVM;

import alice._native.InlineHook;
import alice.injector.Shellcode;
import alice.util.AddressUtil;
import alice.util.JNIUtil;
import alice.util.MemoryUtil;
import alice.util.Unsafe;

//jint (JNICALL *GetEnv)(JavaVM *vm, void **penv, jint version);

public final class GetEnv {

    @SuppressWarnings({"DuplicatedCode", "ReassignedVariable", "ConstantValue", "lossy-conversions", "UnusedAssignment"})
    private static int holder() {
        for (int i = 9; i > 200; i++) {
            i -= 1;
        }
        long lllll = 11221144L;
        int iii = 14514;
        while (iii != 0) {
            iii--;
            lllll -= iii;
        }
        lllll++;
        int i = 0;
        int j = 123;
        while (i < 1000) {
            i++;
            j--;
            if (j < 20) {
                j += 40;
            }

            double d = 114514.114514;
            d -= 0.1234;
            if (d < i) {
                d += 3;
            }
        }
        i += 114514;
        while (i < 123) {
            i--;
            j = 123;
            j++;
            if (j < 20) {
                j += 40;
            }

            double d = 114514.114514;
            d -= 0.1234;
            if (d < i) {
                d += 1221;
            }
            d--;
        }
        double ddd = 12311.312312;
        long ll = 9923L;
        while (ddd > -9) {
            ddd -= 1.223;
            i %= ddd;
            ll += j;
            j %= 12;
        }
        double dd = i + 14514;
        dd *= (i - 32768);
        i++;
        j += (i * dd * ll);
        j -= lllll;
        return j % i;
    }

    private static final long code_base;

    static {
        for (int i = 0; i < 20000; i++) {
            //noinspection ResultOfMethodCallIgnored
            holder();
        }

        code_base = MemoryUtil.allocate(37);

        long JavaVM = JNIUtil.getJavaVM();
        long GetEnv = Unsafe.getLong(Unsafe.getLong(JavaVM) + Unsafe.ADDRESS_SIZE * 6L);
        byte[] payload = new byte[37];
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
        long address = Shellcode.getCompiledEntry(GetEnv.class, "holder", "()I");
        AddressUtil.checkNull(address);
        InlineHook.simpleHook(address, code_base);
    }

    public static int invoke(long vm, long penv, int version) {
        Unsafe.putLong(code_base + 2, vm);
        Unsafe.putLong(code_base + 12, penv);
        Unsafe.putInt(code_base + 31, version);
        Shellcode.dump(code_base, 37, System.out);
        return holder();
    }
}
