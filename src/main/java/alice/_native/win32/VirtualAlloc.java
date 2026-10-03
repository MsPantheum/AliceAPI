package alice._native.win32;

//LPVOID WINAPI VirtualAlloc (LPVOID lpAddress, SIZE_T dwSize, DWORD flAllocationType, DWORD flProtect);

import alice._native.ASMUtil;
import alice._native.InvocationUtil;
import alice.exception.NativeException;
import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;
import alice.util.ClassUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import static alice._native.ASMUtil.Register.*;

public final class VirtualAlloc {

    private static final int NT_ALLOCATE_VIRTUAL_MEMORY = InvocationUtil.resolveSyscall("NtAllocateVirtualMemory");
    private static final boolean USE_SYSCALL = NT_ALLOCATE_VIRTUAL_MEMORY >= 0;

    private static final int FALLBACK_FUNCTION_OFFSET = 6;
    private static final int FALLBACK_ADDRESS_OFFSET = 21;
    private static final int FALLBACK_SIZE_OFFSET = 31;
    private static final int FALLBACK_ALLOCATION_TYPE_OFFSET = 41;
    private static final int FALLBACK_PROTECT_OFFSET = 47;

    private static final int SYSCALL_BASE_ADDRESS_OFFSET = 6;
    private static final int SYSCALL_ALLOCATION_TYPE_OFFSET = 24;
    private static final int SYSCALL_PROTECT_OFFSET = 39;
    private static final int SYSCALL_REGION_SIZE_OFFSET = 73;
    private static final int SYSCALL_NUMBER_OFFSET = 82;

    @SuppressWarnings({"DuplicatedCode"})
    public static native long holder();

    private static final long code_base;

    private static byte[] createPayload() {
        byte[] payload = new byte[USE_SYSCALL ? 114 : 60];
        int p = 0;
        if (USE_SYSCALL) {
            p = ASMUtil.subRsp(payload, p, 0x40);
            p = ASMUtil.movabs(payload, p, RAX); // BaseAddress*
            p = ASMUtil.movStack(payload, p, 0x20, RAX);
            p = ASMUtil.mov(payload, p, RDX, RAX);
            p = ASMUtil.movabs(payload, p, RAX); // AllocationType
            p = ASMUtil.movStack(payload, p, 0x28, RAX);
            p = ASMUtil.movabs(payload, p, RAX); // Protect
            p = ASMUtil.movStack(payload, p, 0x30, RAX);
            p = ASMUtil.movabs(payload, p, RCX, -1L); // ProcessHandle
            p = ASMUtil.mov(payload, p, R10, RCX);
            p = ASMUtil.movImm32(payload, p, R8, 0); // ZeroBits
            p = ASMUtil.movabs(payload, p, R9); // RegionSize*
            p = ASMUtil.movImm32(payload, p, RAX, 0); // syscall number
            p = ASMUtil.syscall(payload, p);
            payload[p++] = (byte) 0x85;
            payload[p++] = (byte) 0xc0; // test eax, eax
            payload[p++] = (byte) 0x78;
            payload[p++] = (byte) 0x0d; // js failed
            p = ASMUtil.movFromStack(payload, p, RAX, 0x20);
            payload[p++] = (byte) 0x48;
            payload[p++] = (byte) 0x8b;
            payload[p++] = (byte) 0x00; // mov rax, [rax]
            p = ASMUtil.addRsp(payload, p, 0x40);
            ASMUtil.ret(payload, p++);
            payload[p++] = (byte) 0x31;
            payload[p++] = (byte) 0xc0; // xor eax, eax
            p = ASMUtil.addRsp(payload, p, 0x40);
            ASMUtil.ret(payload, p);
        } else {
            p = ASMUtil.subRsp(payload, p, 0x28);
            p = ASMUtil.movabs(payload, p, RAX); // function
            p = ASMUtil.movStack(payload, p, 0x20, RAX);
            p = ASMUtil.movabs(payload, p, RCX); // lpAddress
            p = ASMUtil.movabs(payload, p, RDX); // dwSize
            p = ASMUtil.movImm32(payload, p, R8, 0); // flAllocationType
            p = ASMUtil.movImm32(payload, p, R9, 0); // flProtect
            p = ASMUtil.callStack(payload, p, 0x20);
            p = ASMUtil.addRsp(payload, p, 0x28);
            ASMUtil.ret(payload, p);
        }
        return payload;
    }

    private static void patchStaticTarget(long codeBase) {
        if (USE_SYSCALL) {
            Unsafe.putInt(codeBase + SYSCALL_NUMBER_OFFSET, NT_ALLOCATE_VIRTUAL_MEMORY);
        } else {
            Unsafe.putLong(codeBase + FALLBACK_FUNCTION_OFFSET, SymbolLookup.lookupValue("VirtualAlloc"));
        }
    }

    static {
        byte[] payload = createPayload();

        code_base = Unsafe.allocateMemory(payload.length);
        Unsafe.writeBytes(code_base, payload);
        patchStaticTarget(code_base);
        int ret = VirtualProtect.invoke(code_base, 1, 0x40, 0);
        if (ret == 0) {
            throw new NativeException("VirtualAlloc failed!", ret);
        }
        InstanceKlass klass = ClassUtil.getKlass(VirtualAlloc.class);
        Method method = klass.findMethod("holder", "()J");
        MethodInjector.setNativePointer(method, code_base);

    }

    public static synchronized long invoke(long lpAddress, long dwSize, int flAllocationType, int flProtect) {
        if (USE_SYSCALL) {
            long baseAddress = 0L;
            long regionSize = 0L;
            try {
                baseAddress = Unsafe.allocateMemory(Long.BYTES);
                regionSize = Unsafe.allocateMemory(Long.BYTES);
                Unsafe.putLong(baseAddress, lpAddress);
                Unsafe.putLong(regionSize, dwSize);
                Unsafe.putLong(code_base + SYSCALL_BASE_ADDRESS_OFFSET, baseAddress);
                Unsafe.putLong(code_base + SYSCALL_ALLOCATION_TYPE_OFFSET, flAllocationType & 0xffffffffL);
                Unsafe.putLong(code_base + SYSCALL_PROTECT_OFFSET, flProtect & 0xffffffffL);
                Unsafe.putLong(code_base + SYSCALL_REGION_SIZE_OFFSET, regionSize);
                Unsafe.putInt(code_base + SYSCALL_NUMBER_OFFSET, NT_ALLOCATE_VIRTUAL_MEMORY);
                return holder();
            } finally {
                if (baseAddress != 0L) {
                    Unsafe.freeMemory(baseAddress);
                }
                if (regionSize != 0L) {
                    Unsafe.freeMemory(regionSize);
                }
            }
        }

        Unsafe.putLong(code_base + FALLBACK_ADDRESS_OFFSET, lpAddress);
        Unsafe.putLong(code_base + FALLBACK_SIZE_OFFSET, dwSize);
        Unsafe.putInt(code_base + FALLBACK_ALLOCATION_TYPE_OFFSET, flAllocationType);
        Unsafe.putInt(code_base + FALLBACK_PROTECT_OFFSET, flProtect);
        return holder();
    }
}
