package alice._native.win32;

//LPVOID WINAPI VirtualAlloc (LPVOID lpAddress, SIZE_T dwSize, DWORD flAllocationType, DWORD flProtect);

import alice._native.ASMUtil;
import alice.exception.NativeException;
import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;
import alice.util.ClassUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import static alice._native.ASMUtil.Register.*;

public final class VirtualAlloc {

    @SuppressWarnings({"DuplicatedCode"})
    public static native long holder();

    private static final long code_base;

    static {
        holder();
        byte[] payload = new byte[60];
        int p = 0;
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

        code_base = Unsafe.allocateMemory(payload.length);
        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 6, SymbolLookup.lookup("VirtualAlloc"));
        int ret = VirtualProtect.invoke(code_base, 1, 0x40, 0);
        if (ret == 0) {
            throw new NativeException("VirtualAlloc failed!", ret);
        }
        InstanceKlass klass = ClassUtil.getKlass(VirtualAlloc.class);
        Method method = klass.findMethod("holder", "()J");
        MethodInjector.setNativePointer(method, code_base);

    }

    public static synchronized long invoke(long lpAddress, long dwSize, int flAllocationType, int flProtect) {
        Unsafe.putLong(code_base + 21, lpAddress);
        Unsafe.putLong(code_base + 31, dwSize);
        Unsafe.putInt(code_base + 41, flAllocationType);
        Unsafe.putInt(code_base + 47, flProtect);
        return holder();
    }
}
