package alice._native.win32;

//SIZE_T WINAPI VirtualQuery (LPCVOID lpAddress, PMEMORY_BASIC_INFORMATION lpBuffer, SIZE_T dwLength);

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

public class VirtualQuery {

    private static final int NT_QUERY_VIRTUAL_MEMORY = InvocationUtil.resolveSyscall("NtQueryVirtualMemory");
    private static final boolean USE_SYSCALL = NT_QUERY_VIRTUAL_MEMORY >= 0;

    private static final int FALLBACK_FUNCTION_OFFSET = 6;
    private static final int FALLBACK_ADDRESS_OFFSET = 21;
    private static final int FALLBACK_BUFFER_OFFSET = 31;
    private static final int FALLBACK_LENGTH_OFFSET = 41;

    private static final int SYSCALL_RETURN_LENGTH_OFFSET = 6;
    private static final int SYSCALL_ADDRESS_OFFSET = 34;
    private static final int SYSCALL_BUFFER_OFFSET = 50;
    private static final int SYSCALL_LENGTH_OFFSET = 60;
    private static final int SYSCALL_NUMBER_OFFSET = 74;

    private static native long holder();

    private static final long code_base;

    private static byte[] createPayload() {
        byte[] payload = new byte[USE_SYSCALL ? 104 : 58];
        int p = 0;
        if (USE_SYSCALL) {
            p = ASMUtil.subRsp(payload, p, 0x40);
            p = ASMUtil.movabs(payload, p, RAX); // ReturnLength*
            p = ASMUtil.movStack(payload, p, 0x30, RAX);
            p = ASMUtil.movabs(payload, p, RCX, -1L); // ProcessHandle
            p = ASMUtil.mov(payload, p, R10, RCX);
            p = ASMUtil.movabs(payload, p, RDX); // BaseAddress
            p = ASMUtil.movImm32(payload, p, R8, 0); // MemoryBasicInformation
            p = ASMUtil.movabs(payload, p, R9); // MemoryInformation
            p = ASMUtil.movabs(payload, p, RAX); // MemoryInformationLength
            p = ASMUtil.movStack(payload, p, 0x28, RAX);
            p = ASMUtil.movImm32(payload, p, RAX, 0); // syscall number
            p = ASMUtil.syscall(payload, p);
            payload[p++] = (byte) 0x85;
            payload[p++] = (byte) 0xc0; // test eax, eax
            payload[p++] = (byte) 0x78;
            payload[p++] = (byte) 0x0d; // js failed
            p = ASMUtil.movFromStack(payload, p, RAX, 0x30);
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
            p = ASMUtil.movabs(payload, p, RDX); // lpBuffer
            p = ASMUtil.movabs(payload, p, R8); // dwLength
            p = ASMUtil.callStack(payload, p, 0x20);
            p = ASMUtil.addRsp(payload, p, 0x28);
            ASMUtil.ret(payload, p);
        }
        return payload;
    }

    private static void patchStaticTarget(long codeBase) {
        if (USE_SYSCALL) {
            Unsafe.putInt(codeBase + SYSCALL_NUMBER_OFFSET, NT_QUERY_VIRTUAL_MEMORY);
        } else {
            Unsafe.putLong(codeBase + FALLBACK_FUNCTION_OFFSET, SymbolLookup.lookup("VirtualQuery"));
        }
    }

    static {
        byte[] payload = createPayload();
        code_base = MemoryUtil.allocate(payload.length);
        Unsafe.writeBytes(code_base, payload);
        patchStaticTarget(code_base);
        InstanceKlass klass = ClassUtil.getKlass(VirtualQuery.class);
        Method method = klass.findMethod("holder", "()J");
        MethodInjector.setNativePointer(method, code_base);
    }

    public static synchronized long invoke(long lpAddress, long lpBuffer, long dwLength) {
        if (USE_SYSCALL) {
            long returnLength = 0L;
            try {
                returnLength = Unsafe.allocateMemory(Long.BYTES);
                Unsafe.putLong(returnLength, 0L);
                Unsafe.putLong(code_base + SYSCALL_RETURN_LENGTH_OFFSET, returnLength);
                Unsafe.putLong(code_base + SYSCALL_ADDRESS_OFFSET, lpAddress);
                Unsafe.putLong(code_base + SYSCALL_BUFFER_OFFSET, lpBuffer);
                Unsafe.putLong(code_base + SYSCALL_LENGTH_OFFSET, dwLength);
                Unsafe.putInt(code_base + SYSCALL_NUMBER_OFFSET, NT_QUERY_VIRTUAL_MEMORY);
                return holder();
            } finally {
                if (returnLength != 0L) {
                    Unsafe.freeMemory(returnLength);
                }
            }
        }

        Unsafe.putLong(code_base + FALLBACK_ADDRESS_OFFSET, lpAddress);
        Unsafe.putLong(code_base + FALLBACK_BUFFER_OFFSET, lpBuffer);
        Unsafe.putLong(code_base + FALLBACK_LENGTH_OFFSET, dwLength);
        return holder();
    }
}
