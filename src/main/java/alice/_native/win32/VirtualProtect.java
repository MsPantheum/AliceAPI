package alice._native.win32;

import alice._native.ASMUtil;
import alice._native.InvocationUtil;
import alice.exception.NativeException;
import alice.exception.ShouldNotReachHere;
import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;
import alice.util.ClassUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import static alice._native.ASMUtil.Register.*;

//WINBASEAPI WINBOOL WINAPI VirtualProtect (LPVOID lpAddress, SIZE_T dwSize, DWORD flNewProtect, PDWORD lpflOldProtect);

public final class VirtualProtect {

    private static final int NT_PROTECT_VIRTUAL_MEMORY = InvocationUtil.resolveSyscall("NtProtectVirtualMemory");
    private static final boolean USE_SYSCALL = NT_PROTECT_VIRTUAL_MEMORY >= 0;

    private static final int FALLBACK_LP_ADDRESS_OFFSET = 6;
    private static final int FALLBACK_DW_SIZE_OFFSET = 16;
    private static final int FALLBACK_OLD_PROTECT_OFFSET = 26;
    private static final int FALLBACK_FUNCTION_OFFSET = 36;
    private static final int FALLBACK_NEW_PROTECT_OFFSET = 46;

    private static final int SYSCALL_OLD_PROTECT_OFFSET = 6;
    private static final int SYSCALL_BASE_ADDRESS_OFFSET = 34;
    private static final int SYSCALL_REGION_SIZE_OFFSET = 44;
    private static final int SYSCALL_NEW_PROTECT_OFFSET = 54;
    private static final int SYSCALL_NUMBER_OFFSET = 59;

    private static int ntSuccessToWinBool(byte[] payload, int index) {
        int p = index;
        payload[p++] = (byte) 0x85;
        payload[p++] = (byte) 0xc0; // test eax, eax
        payload[p++] = (byte) 0x0f;
        payload[p++] = (byte) 0x99;
        payload[p++] = (byte) 0xc0; // setns al
        payload[p++] = (byte) 0x0f;
        payload[p++] = (byte) 0xb6;
        payload[p++] = (byte) 0xc0; // movzx eax, al
        return p;
    }

    private static byte[] createPayload() {
        byte[] payload = new byte[USE_SYSCALL ? 78 : 62];
        int p = 0;
        if (USE_SYSCALL) {
            p = ASMUtil.subRsp(payload, p, 0x30);
            p = ASMUtil.movabs(payload, p, RAX); // lpflOldProtect
            p = ASMUtil.movStack(payload, p, 0x28, RAX);
            p = ASMUtil.movabs(payload, p, RCX, -1L); // ProcessHandle
            p = ASMUtil.mov(payload, p, R10, RCX);
            p = ASMUtil.movabs(payload, p, RDX); // BaseAddress*
            p = ASMUtil.movabs(payload, p, R8); // RegionSize*
            p = ASMUtil.movImm32(payload, p, R9, 0); // NewProtect
            p = ASMUtil.movImm32(payload, p, RAX, 0); // syscall number
            p = ASMUtil.syscall(payload, p);
            p = ASMUtil.addRsp(payload, p, 0x30);
            p = ntSuccessToWinBool(payload, p);
            ASMUtil.ret(payload, p);
        } else {
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
        }
        return payload;
    }

    private static void patchStaticTarget(long codeBase) {
        if (USE_SYSCALL) {
            Unsafe.putInt(codeBase + SYSCALL_NUMBER_OFFSET, NT_PROTECT_VIRTUAL_MEMORY);
        } else {
            Unsafe.putLong(codeBase + FALLBACK_FUNCTION_OFFSET, SymbolLookup.lookupValue("VirtualProtect"));
        }
    }

    private static int invokeFallback(long codeBase, long lpAddress, long dwSize, int flNewProtect, long lpflOldProtect, boolean bootstrap) {
        boolean allocatedOldProtect = false;
        try {
            Unsafe.putLong(codeBase + FALLBACK_LP_ADDRESS_OFFSET, lpAddress);
            Unsafe.putLong(codeBase + FALLBACK_DW_SIZE_OFFSET, dwSize);
            Unsafe.putInt(codeBase + FALLBACK_NEW_PROTECT_OFFSET, flNewProtect);

            if (lpflOldProtect == 0) {
                allocatedOldProtect = true;
                lpflOldProtect = Unsafe.allocateMemory(Integer.BYTES);
            }

            Unsafe.putLong(codeBase + FALLBACK_OLD_PROTECT_OFFSET, lpflOldProtect);
            return bootstrap ? Bootstrap.holder() : holder();
        } finally {
            if (allocatedOldProtect) {
                Unsafe.freeMemory(lpflOldProtect);
            }
        }
    }

    private static int invokeSyscall(long codeBase, long lpAddress, long dwSize, int flNewProtect, long lpflOldProtect, boolean bootstrap) {
        long baseAddress = 0L;
        long regionSize = 0L;
        boolean allocatedOldProtect = false;
        try {
            baseAddress = Unsafe.allocateMemory(Long.BYTES);
            regionSize = Unsafe.allocateMemory(Long.BYTES);
            if (lpflOldProtect == 0) {
                allocatedOldProtect = true;
                lpflOldProtect = Unsafe.allocateMemory(Integer.BYTES);
            }

            Unsafe.putLong(baseAddress, lpAddress);
            Unsafe.putLong(regionSize, dwSize);

            Unsafe.putLong(codeBase + SYSCALL_OLD_PROTECT_OFFSET, lpflOldProtect);
            Unsafe.putLong(codeBase + SYSCALL_BASE_ADDRESS_OFFSET, baseAddress);
            Unsafe.putLong(codeBase + SYSCALL_REGION_SIZE_OFFSET, regionSize);
            Unsafe.putInt(codeBase + SYSCALL_NEW_PROTECT_OFFSET, flNewProtect);
            Unsafe.putInt(codeBase + SYSCALL_NUMBER_OFFSET, NT_PROTECT_VIRTUAL_MEMORY);
            return bootstrap ? Bootstrap.holder() : holder();
        } finally {
            if (allocatedOldProtect) {
                Unsafe.freeMemory(lpflOldProtect);
            }
            if (baseAddress != 0L) {
                Unsafe.freeMemory(baseAddress);
            }
            if (regionSize != 0L) {
                Unsafe.freeMemory(regionSize);
            }
        }
    }

    private static synchronized int invokeCode(long codeBase, long lpAddress, long dwSize, int flNewProtect, long lpflOldProtect, boolean bootstrap) {
        return USE_SYSCALL
                ? invokeSyscall(codeBase, lpAddress, dwSize, flNewProtect, lpflOldProtect, bootstrap)
                : invokeFallback(codeBase, lpAddress, dwSize, flNewProtect, lpflOldProtect, bootstrap);
    }

    private static class Bootstrap {
        private static boolean invalidated;

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
        private static final byte[] backup;

        static {
            for (int i = 0; i < 20000; i++) {
                //noinspection ResultOfMethodCallIgnored
                holder();
            }

            byte[] payload = createPayload();
            backup = new byte[payload.length];
            code_base = MethodInjector.inject(payload, Bootstrap.class, "holder", "()I", true, backup);
            patchStaticTarget(code_base);
        }

        private synchronized static int invoke(long lpAddress, long dwSize, int flNewProtect, long lpflOldProtect) {
            if (invalidated) {
                throw new ShouldNotReachHere();
            }
            return invokeCode(code_base, lpAddress, dwSize, flNewProtect, lpflOldProtect, true);
        }

        private synchronized static void invalidate() {
            if (invalidated) {
                throw new ShouldNotReachHere();
            }
            Unsafe.writeBytes(code_base, backup);
            invalidated = true;
        }
    }

    private static native int holder();

    private static final long code_base;

    static {
        byte[] payload = createPayload();

        code_base = Unsafe.allocateMemory(payload.length);
        Unsafe.writeBytes(code_base, payload);
        patchStaticTarget(code_base);

        int ret = Bootstrap.invoke(code_base, 1, 0x40, 0);
        if (ret == 0) {
            throw new NativeException("VirtualProtect failed!", ret);
        }
        Bootstrap.invalidate();
        InstanceKlass klass = ClassUtil.getKlass(VirtualProtect.class);
        Method method = klass.findMethod("holder", "()I");
        MethodInjector.setNativePointer(method, code_base);

    }

    public synchronized static int invoke(long lpAddress, long dwSize, int flNewProtect, long lpflOldProtect) {
        return invokeCode(code_base, lpAddress, dwSize, flNewProtect, lpflOldProtect, false);
    }
}
