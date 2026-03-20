package alice._native.jni;

import alice.Platform;
import alice._native.InlineHook;
import alice._native.linux.mmap;
import alice._native.win32.VirtualAlloc;
import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.Unsafe;

import static alice.util.Constants.*;

//JNI_GetCreatedJavaVMs(JavaVM **, jsize, jsize *);

public class JNI_GetCreatedJavaVMs {

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

        byte[] payload = new byte[37];
        payload[0] = (byte) 0x48;
        payload[1] = (byte) 0xbf;
        //vmBuf here
        payload[10] = (byte) 0x48;
        payload[11] = (byte) 0xba;
        //nVMs here
        payload[20] = (byte) 0x48;
        payload[21] = (byte) 0xb8;
        //function
        payload[30] = (byte) 0xbe;
        //bufLen here
        payload[35] = (byte) 0xff;
        payload[36] = (byte) 0xe0;
        code_base = Platform.win32 ? VirtualAlloc.invoke(0, 37, MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE) : mmap.invoke(0, 37, PROT_READ | PROT_WRITE | PROT_EXEC, MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 22, SymbolLookup.lookup("JNI_GetCreatedJavaVMs"));
        long address = Shellcode.getCompiledEntry(JNI_GetCreatedJavaVMs.class, "holder", "()I");
        assert address != 0;
        InlineHook.simpleHook(address, code_base);
    }

    public static int invoke(long vmBuf, int bufLen, long nVMs) {
        Unsafe.putLong(code_base + 2, vmBuf);
        Unsafe.putInt(code_base + 31, bufLen);
        Unsafe.putLong(code_base + 12, nVMs);
        return holder();
    }
}
