package alice._native.linux;

import alice._native.InlineHook;
import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.AddressUtil;
import alice.util.Unsafe;

import static alice.util.constants.Constants.*;

//void *mmap (void *__addr, size_t __len, int __prot,int __flags, int __fd, __off_t __offset)

public class mmap {

    @SuppressWarnings({"DuplicatedCode", "ReassignedVariable", "ConstantValue", "lossy-conversions", "UnusedAssignment"})
    private static long holder() {
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

        byte[] payload = new byte[58];
        payload[0] = (byte) 0x48;
        payload[1] = (byte) 0xbf;
        //__addr here
        payload[10] = (byte) 0x48;
        payload[11] = (byte) 0xbe;
        //__len here
        payload[20] = (byte) 0x49;
        payload[21] = (byte) 0xb9;
        //__offset here
        payload[30] = (byte) 0x48;
        payload[31] = (byte) 0xb8;
        //function
        payload[40] = (byte) 0xba;
        //__prot here
        payload[45] = (byte) 0xb9;
        //__flags here
        payload[50] = (byte) 0x41;
        payload[51] = (byte) 0xb8;
        //__fd here
        payload[56] = (byte) 0xff;
        payload[57] = (byte) 0xe0;
        code_base = Unsafe.allocateMemory(58);
        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 32, SymbolLookup.lookup("mmap"));
        int success = mprotect.invoke(AddressUtil.align(code_base), 1, PROT_READ | PROT_WRITE | PROT_EXEC);
        assert success == 0;
        long address = Shellcode.getCompiledEntry(mmap.class, "holder", "()J");
        InlineHook.simpleHook(address, code_base);
    }

    public synchronized static long invoke(long __addr, long __len, int __prot, int __flags, int __fd, long __offset) {
        Unsafe.putLong(code_base + 2, __addr);
        Unsafe.putLong(code_base + 12, __len);
        Unsafe.putLong(code_base + 22, __offset);
        Unsafe.putInt(code_base + 41, __prot);
        Unsafe.putInt(code_base + 46, __flags);
        Unsafe.putInt(code_base + 52, __fd);
        return holder();
    }

}
