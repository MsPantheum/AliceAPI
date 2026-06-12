package alice._native.win32;

import alice._native.ASMUtil;
import alice.exception.NativeException;
import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;
import alice.util.ClassUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import static alice._native.ASMUtil.Register.*;

//WINBASEAPI WINBOOL WINAPI VirtualProtect (LPVOID lpAddress, SIZE_T dwSize, DWORD flNewProtect, PDWORD lpflOldProtect);

public final class VirtualProtect {

    private static class Bootstrap {
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

            byte[] payload = new byte[62];
            int p = 0;
            p = ASMUtil.subRsp(payload, p, 0x28);
            p = ASMUtil.movabs(payload, p, RCX); // lpAddress
            p = ASMUtil.movabs(payload, p, RDX); // dwSize
            p = ASMUtil.movabs(payload, p, R9); // lpflOldProtect
            p = ASMUtil.movabs(payload, p, RAX); // function
            p = ASMUtil.movImm32(payload, p, R8, 0); // flNewProtect
            p = ASMUtil.call(payload, p, RAX);
            p = ASMUtil.movImm32(payload, p, RAX, 1);
            p = ASMUtil.addRsp(payload, p, 0x28);
            ASMUtil.ret(payload, p);

            code_base = MethodInjector.inject(payload, Bootstrap.class, "holder", "()I", false);
            Unsafe.putLong(code_base + 36, SymbolLookup.lookup("VirtualProtect"));
        }

        @SuppressWarnings({"SameParameterValue", "DuplicatedCode"})
        private static int invoke(long lpAddress, int dwSize, int flNewProtect, long lpflOldProtect) {
            Unsafe.putLong(code_base + 6, lpAddress);
            Unsafe.putInt(code_base + 16, dwSize);
            Unsafe.putInt(code_base + 46, flNewProtect);
            boolean allocated = false;
            if (lpflOldProtect == 0) {
                allocated = true;
                lpflOldProtect = Unsafe.allocateMemory(Integer.BYTES);
            }
            Unsafe.putLong(code_base + 26, lpflOldProtect);
            if (allocated) {
                Unsafe.freeMemory(lpflOldProtect);
            }
            return holder();
        }
    }

    private static native int holder();

    private static final long code_base;

    static {
        byte[] payload = new byte[62];
        int p = 0;
        p = ASMUtil.subRsp(payload, p, 0x28);
        p = ASMUtil.movabs(payload, p, RCX); // lpAddress
        p = ASMUtil.movabs(payload, p, RDX); // dwSize
        p = ASMUtil.movabs(payload, p, R9); // lpflOldProtect
        p = ASMUtil.movabs(payload, p, RAX); // function
        p = ASMUtil.movImm32(payload, p, R8, 0); // flNewProtect
        p = ASMUtil.call(payload, p, RAX);
        p = ASMUtil.movImm32(payload, p, RAX, 1);
        p = ASMUtil.addRsp(payload, p, 0x28);
        ASMUtil.ret(payload, p);

        code_base = Unsafe.allocateMemory(payload.length);
        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 36, SymbolLookup.lookup("VirtualProtect"));
        int ret = Bootstrap.invoke(code_base, 1, 0x40, 0);
        if (ret == 0) {
            throw new NativeException("VirtualProtect failed!", ret);
        }
        InstanceKlass klass = ClassUtil.getKlass(VirtualProtect.class);
        Method method = klass.findMethod("holder", "()I");
        MethodInjector.setNativePointer(method, code_base);

    }

    @SuppressWarnings("DuplicatedCode")
    public synchronized static int invoke(long lpAddress, long dwSize, int flNewProtect, long lpflOldProtect) {
        Unsafe.putLong(code_base + 6, lpAddress);
        Unsafe.putLong(code_base + 16, dwSize);
        Unsafe.putInt(code_base + 46, flNewProtect);
        boolean allocated = false;
        if (lpflOldProtect == 0) {
            allocated = true;
            lpflOldProtect = Unsafe.allocateMemory(Integer.BYTES);
        }
        Unsafe.putLong(code_base + 26, lpflOldProtect);
        if (allocated) {
            Unsafe.freeMemory(lpflOldProtect);
        }
        return holder();
    }
}
