package alice._native.linux;

//int mprotect (void *__addr, size_t __len, int __prot)

import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.AddressUtil;
import alice.util.ClassUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import static alice.util.constants.Constants.*;

public final class mprotect {

    private static class Bootstrap {
        @SuppressWarnings({"DuplicatedCode", "ReassignedVariable", "ConstantValue", "lossy-conversions", "UnusedAssignment", "ResultOfMethodCallIgnored"})
        private static int holder() {
            for (int i = 9; i > 200; i++) {
                i -= 1;
            }
            Runtime.getRuntime();
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
            System.getProperties();
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
            System.nanoTime();
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
            System.currentTimeMillis();
            j += (i * dd * ll);
            j -= lllll;
            return j;
        }

        private static final long code_base;

        static {
            for (int i = 0; i < 40000; i++) {
                holder();
            }
            byte[] payload = new byte[37];
            payload[0] = (byte) 0x48;
            payload[1] = (byte) 0xbf;
            //__addr here
            payload[10] = (byte) 0x48;
            payload[11] = (byte) 0xbe;
            //__len here
            payload[20] = (byte) 0x48;
            payload[21] = (byte) 0xb8;
            //function
            payload[30] = (byte) 0xba;
            //__prot here
            payload[35] = (byte) 0xff;
            payload[36] = (byte) 0xe0;
            code_base = Shellcode.inject(payload, Bootstrap.class, "holder", "()I");
            AddressUtil.checkNull(code_base);
            Unsafe.putLong(code_base + 22, SymbolLookup.lookup("mprotect"));
        }

        private synchronized static int invoke(long __addr, long __len, int __prot) {
            Unsafe.putLong(code_base + 2, __addr);
            Unsafe.putLong(code_base + 12, __len);
            Unsafe.putInt(code_base + 31, __prot);
            return holder();
        }
    }

    private static int holder() {
        return (int) System.nanoTime();
    }

    private static final long code_base;

    static {
        byte[] payload = new byte[37];
        code_base = Unsafe.allocateMemory(payload.length);
        payload[0] = (byte) 0x48;
        payload[1] = (byte) 0xbf;
        //__addr here
        payload[10] = (byte) 0x48;
        payload[11] = (byte) 0xbe;
        //__len here
        payload[20] = (byte) 0x48;
        payload[21] = (byte) 0xb8;
        //function
        payload[30] = (byte) 0xba;
        //__prot here
        payload[35] = (byte) 0xff;
        payload[36] = (byte) 0xe0;
        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 22, SymbolLookup.lookup("mprotect"));
        int ret = Bootstrap.invoke(AddressUtil.align(code_base), 1, PROT_EXEC | PROT_WRITE | PROT_READ);
        if (ret != 0) {
            System.out.println(ret);
            throw new IllegalStateException();
        }
        InstanceKlass klass = ClassUtil.getKlass(mprotect.class);
        Method method = klass.findMethod("holder", "()I");
        Shellcode.antiOptimization(method);
        Shellcode.setInterpretedEntry(method, code_base);
    }

    public synchronized static int invoke(long __addr, long __len, int __prot) {
        Unsafe.putLong(code_base + 2, __addr);
        Unsafe.putLong(code_base + 12, __len);
        Unsafe.putInt(code_base + 31, __prot);
        return holder();
    }

}
