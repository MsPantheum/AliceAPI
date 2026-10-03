package alice._native.linux;

import alice.Platform;
import alice._native.ASMUtil;
import alice.exception.NativeException;
import alice.exception.ShouldNotReachHere;
import alice.injector.MethodInjector;
import alice.util.AddressUtil;
import alice.util.ClassUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import static alice._native.ASMUtil.Register.*;
import static alice.util.constants.Constants.*;

//void *mmap (void *__addr, size_t __len, int __prot,int __flags, int __fd, __off_t __offset)

public final class mmap {

    private static final int LINUX_SYSCALL_TRAMPOLINE_SIZE = 55;
    private static final int FREEBSD_SYSCALL_TRAMPOLINE_SIZE = 60;
    private static final int TRAMPOLINE_SIZE = Platform.bsd ? FREEBSD_SYSCALL_TRAMPOLINE_SIZE : LINUX_SYSCALL_TRAMPOLINE_SIZE;
    private static final int ARG0_OFFSET = 2;
    private static final int ARG1_OFFSET = 12;
    private static final int ARG5_OFFSET = 22;
    private static final int SYSCALL_OFFSET = 31;
    private static final int ARG2_OFFSET = 36;
    private static final int ARG3_OFFSET = 42;
    private static final int ARG4_OFFSET = 48;
    private static final int LINUX_SYS_MMAP = 9;
    private static final int LINUX_SYS_MPROTECT = 10;
    private static final int FREEBSD_SYS_MPROTECT = 74;
    private static final int FREEBSD_SYS_MMAP = 477;
    private static final int BSD_MAP_ANON = 0x1000;
    private static final int LINUX_MAP_ANONYMOUS = 0x20;
    private static final long code_base;

    static {
        byte[] payload = new byte[TRAMPOLINE_SIZE];
        emitPayload(payload);
        code_base = Bootstrap.invoke();
        if (isMapFailed(code_base)) {
            throw new NativeException(Bootstrap.mmapFailureDetails(), -1);
        }
        Unsafe.writeBytes(code_base, payload);
        int mprotect = Bootstrap.mprotect(code_base, payload.length, PROT_READ | PROT_WRITE | PROT_EXEC);
        if (mprotect != 0) {
            throw new NativeException("mprotect failed!", mprotect);
        }
        InstanceKlass klass = ClassUtil.getKlass(mmap.class);
        Method method = klass.findMethod("holder", "()J");
        MethodInjector.setNativePointer(method, code_base);
        Bootstrap.invalidate();//Restore it so JVM won't complain later
    }

    @SuppressWarnings(value = {"DuplicatedCode"})
    private static native long holder();

    public synchronized static long invoke(long __addr, long __len, int __prot, int __flags, int __fd, long __offset) {
        Unsafe.putLong(code_base + ARG0_OFFSET, __addr);
        Unsafe.putLong(code_base + ARG1_OFFSET, __len);
        Unsafe.putLong(code_base + ARG5_OFFSET, __offset);
        if (Platform.bsd) {
            Unsafe.putInt(code_base + SYSCALL_OFFSET, FREEBSD_SYS_MMAP);
        } else {
            Unsafe.putInt(code_base + SYSCALL_OFFSET, LINUX_SYS_MMAP);
        }
        Unsafe.putInt(code_base + ARG2_OFFSET, __prot);
        Unsafe.putInt(code_base + ARG3_OFFSET, __flags);
        Unsafe.putInt(code_base + ARG4_OFFSET, __fd);
        long result = holder();
        return isMapFailed(result) ? MAP_FAILED : result;
    }

    private static boolean isMapFailed(long address) {
        return address == MAP_FAILED || address == 0xffffffffL || address < 0;
    }

    private static void emitPayload(byte[] payload) {
        int p = 0;
        p = ASMUtil.movabs(payload, p, RDI); // __addr
        p = ASMUtil.movabs(payload, p, RSI); // __len
        p = ASMUtil.movabs(payload, p, R9); // __offset
        p = ASMUtil.movImm32(payload, p, RAX, 0); // syscall number
        p = ASMUtil.movImm32(payload, p, RDX, 0); // __prot
        p = ASMUtil.movImm32(payload, p, R10, 0); // __flags
        p = ASMUtil.movImm32(payload, p, R8, 0); // __fd
        if (Platform.bsd) {
            emitFreeBsdSyscallReturn(payload, p);
        } else {
            payload[p++] = (byte) 0x0f; // syscall
            payload[p++] = (byte) 0x05;
            ASMUtil.ret(payload, p);
        }
    }

    private static void emitFreeBsdSyscallReturn(byte[] payload, int index) {
        int p = index;
        payload[p++] = (byte) 0x0f; // syscall
        payload[p++] = (byte) 0x05;
        payload[p++] = (byte) 0x73; // jnc ret
        payload[p++] = (byte) 0x03;
        payload[p++] = (byte) 0x48; // neg rax
        payload[p++] = (byte) 0xf7;
        payload[p++] = (byte) 0xd8;
        payload[p] = (byte) 0xc3; // ret
    }

    private static class Bootstrap {
        private static final long code_base;
        private static final byte[] backup = new byte[TRAMPOLINE_SIZE];
        private static long lastMmapResult;
        private static int lastMmapFlags;
        private static boolean invalidated = false;

        static {
            for (int i = 0; i < 40000; i++) {
                holder();
            }
            byte[] payload = new byte[TRAMPOLINE_SIZE];
            emitPayload(payload);
            code_base = MethodInjector.inject(payload, mmap.Bootstrap.class, "holder", "()J", true, backup);
            AddressUtil.checkNull(code_base);
        }

        @SuppressWarnings({"DuplicatedCode", "ReassignedVariable", "ConstantValue", "lossy-conversions", "UnusedAssignment", "ResultOfMethodCallIgnored"})
        private static long holder() {
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

        private synchronized static long invoke() {
            long result = invoke(MAP_PRIVATE | MAP_ANONYMOUS);
            if (!isMapFailed(result) || !Platform.bsd) {
                return result;
            }
            int fallbackAnonymous = MAP_ANONYMOUS == BSD_MAP_ANON ? LINUX_MAP_ANONYMOUS : BSD_MAP_ANON;
            return invoke(MAP_PRIVATE | fallbackAnonymous);
        }

        private static long invoke(int flags) {
            Unsafe.putLong(code_base + ARG0_OFFSET, 0);
            Unsafe.putLong(code_base + ARG1_OFFSET, TRAMPOLINE_SIZE);
            Unsafe.putLong(code_base + ARG5_OFFSET, 0);
            if (Platform.bsd) {
                Unsafe.putInt(code_base + SYSCALL_OFFSET, FREEBSD_SYS_MMAP);
            } else {
                Unsafe.putInt(code_base + SYSCALL_OFFSET, LINUX_SYS_MMAP);
            }
            Unsafe.putInt(code_base + ARG2_OFFSET, PROT_READ | PROT_WRITE | PROT_EXEC);
            Unsafe.putInt(code_base + ARG3_OFFSET, flags);
            Unsafe.putInt(code_base + ARG4_OFFSET, -1);
            lastMmapFlags = flags;
            lastMmapResult = holder();
            return lastMmapResult;
        }

        private synchronized static int mprotect(long address, long size, int prot) {
            if (invalidated) {
                throw new ShouldNotReachHere();
            }
            long page = AddressUtil.align_page(address);
            Unsafe.putLong(code_base + ARG0_OFFSET, page);
            Unsafe.putLong(code_base + ARG1_OFFSET, size + (address - page));
            Unsafe.putLong(code_base + ARG5_OFFSET, 0);
            if (Platform.bsd) {
                Unsafe.putInt(code_base + SYSCALL_OFFSET, FREEBSD_SYS_MPROTECT);
            } else {
                Unsafe.putInt(code_base + SYSCALL_OFFSET, LINUX_SYS_MPROTECT);
            }
            Unsafe.putInt(code_base + ARG2_OFFSET, prot);
            Unsafe.putInt(code_base + ARG3_OFFSET, 0);
            Unsafe.putInt(code_base + ARG4_OFFSET, 0);
            return (int) holder();
        }

        private static String mmapFailureDetails() {
            int errno = getErrno();
            return "mmap failed! result=0x" + Long.toHexString(lastMmapResult) + ", errno=" + errno + ", flags=0x" + Integer.toHexString(lastMmapFlags) + ", MAP_ANONYMOUS=0x" + Integer.toHexString(MAP_ANONYMOUS) + ", os.name=" + System.getProperty("os.name");
        }

        private static int getErrno() {
            if (lastMmapResult < 0) {
                return (int) -lastMmapResult;
            }
            return 0;
        }

        private static void invalidate() {
            if (invalidated) {
                throw new ShouldNotReachHere();
            }
            Unsafe.writeBytes(code_base, backup);
            //MethodInjector.inject(backup, mmap.Bootstrap.class, "holder", "()J", true, null);
            invalidated = true;
        }
    }

}
