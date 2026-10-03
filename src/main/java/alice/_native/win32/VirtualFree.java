package alice._native.win32;

//WINBOOL WINAPI VirtualFree (LPVOID lpAddress, SIZE_T dwSize, DWORD dwFreeType);

import alice._native.ASMUtil;
import alice._native.InvocationUtil;
import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;
import alice.util.ClassUtil;
import alice.util.MemoryUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import static alice._native.ASMUtil.Register.*;

public final class VirtualFree {

    private static final int NT_FREE_VIRTUAL_MEMORY = InvocationUtil.resolveSyscall("NtFreeVirtualMemory");
    private static final boolean USE_SYSCALL = NT_FREE_VIRTUAL_MEMORY >= 0;

    private static final int FALLBACK_FUNCTION_OFFSET = 6;
    private static final int FALLBACK_ADDRESS_OFFSET = 21;
    private static final int FALLBACK_SIZE_OFFSET = 31;
    private static final int FALLBACK_FREE_TYPE_OFFSET = 41;

    private static final int SYSCALL_BASE_ADDRESS_OFFSET = 19;
    private static final int SYSCALL_REGION_SIZE_OFFSET = 29;
    private static final int SYSCALL_FREE_TYPE_OFFSET = 39;
    private static final int SYSCALL_NUMBER_OFFSET = 44;

    private static native int holder();

    private static final long code_base;
    private static final int payload_size;

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
        byte[] payload = new byte[USE_SYSCALL ? 64 : 54];
        int p = 0;
        if (USE_SYSCALL) {
            p = ASMUtil.subRsp(payload, p, 0x28);
            p = ASMUtil.movabs(payload, p, RCX, -1L); // ProcessHandle
            p = ASMUtil.mov(payload, p, R10, RCX);
            p = ASMUtil.movabs(payload, p, RDX); // BaseAddress*
            p = ASMUtil.movabs(payload, p, R8); // RegionSize*
            p = ASMUtil.movImm32(payload, p, R9, 0); // FreeType
            p = ASMUtil.movImm32(payload, p, RAX, 0); // syscall number
            p = ASMUtil.syscall(payload, p);
            p = ASMUtil.addRsp(payload, p, 0x28);
            p = ntSuccessToWinBool(payload, p);
            ASMUtil.ret(payload, p);
        } else {
            p = ASMUtil.subRsp(payload, p, 0x28);
            p = ASMUtil.movabs(payload, p, RAX); // function
            p = ASMUtil.movStack(payload, p, 0x20, RAX);
            p = ASMUtil.movabs(payload, p, RCX); // lpAddress
            p = ASMUtil.movabs(payload, p, RDX); // dwSize
            p = ASMUtil.movImm32(payload, p, R8, 0); // dwFreeType
            p = ASMUtil.callStack(payload, p, 0x20);
            p = ASMUtil.addRsp(payload, p, 0x28);
            ASMUtil.ret(payload, p);
        }
        return payload;
    }

    private static void patchStaticTarget(long codeBase) {
        if (USE_SYSCALL) {
            Unsafe.putInt(codeBase + SYSCALL_NUMBER_OFFSET, NT_FREE_VIRTUAL_MEMORY);
        } else {
            Unsafe.putLong(codeBase + FALLBACK_FUNCTION_OFFSET, SymbolLookup.lookupValue("VirtualFree"));
        }
    }

    static {
        byte[] payload = createPayload();
        payload_size = payload.length;

        code_base = MemoryUtil.allocate(payload.length);
        Unsafe.writeBytes(code_base, payload);
        patchStaticTarget(code_base);
        InstanceKlass klass = ClassUtil.getKlass(VirtualFree.class);
        Method method = klass.findMethod("holder", "()I");
        MethodInjector.setNativePointer(method, code_base);

    }

    public synchronized static int invoke(long lpAddress, long dwSize, int dwFreeType) {
        if (USE_SYSCALL) {
            long baseAddress = 0L;
            long regionSize = 0L;
            try {
                baseAddress = Unsafe.allocateMemory(Long.BYTES);
                regionSize = Unsafe.allocateMemory(Long.BYTES);
                Unsafe.putLong(baseAddress, lpAddress);
                Unsafe.putLong(regionSize, dwSize);
                Unsafe.putLong(code_base + SYSCALL_BASE_ADDRESS_OFFSET, baseAddress);
                Unsafe.putLong(code_base + SYSCALL_REGION_SIZE_OFFSET, regionSize);
                Unsafe.putInt(code_base + SYSCALL_FREE_TYPE_OFFSET, dwFreeType);
                Unsafe.putInt(code_base + SYSCALL_NUMBER_OFFSET, NT_FREE_VIRTUAL_MEMORY);
                MethodInjector.dump(code_base, payload_size, System.out);
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
        Unsafe.putInt(code_base + FALLBACK_FREE_TYPE_OFFSET, dwFreeType);
        MethodInjector.dump(code_base, payload_size, System.out);
        return holder();
    }
}
