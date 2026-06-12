package alice._native.win32;

//WINBOOL WINAPI VirtualFree (LPVOID lpAddress, SIZE_T dwSize, DWORD dwFreeType);

import alice._native.ASMUtil;
import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;
import alice.util.ClassUtil;
import alice.util.MemoryUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import static alice._native.ASMUtil.Register.*;

public final class VirtualFree {

    private static native int holder();

    private static final long code_base;

    static {
        byte[] payload = new byte[54];
        int p = 0;
        p = ASMUtil.subRsp(payload, p, 0x28);
        p = ASMUtil.movabs(payload, p, RAX); // function
        p = ASMUtil.movStack(payload, p, 0x20, RAX);
        p = ASMUtil.movabs(payload, p, RCX); // lpAddress
        p = ASMUtil.movabs(payload, p, RDX); // dwSize
        p = ASMUtil.movImm32(payload, p, R8, 0); // dwFreeType
        p = ASMUtil.callStack(payload, p, 0x20);
        p = ASMUtil.addRsp(payload, p, 0x28);
        ASMUtil.ret(payload, p);

        code_base = MemoryUtil.allocate(payload.length);
        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 6, SymbolLookup.lookup("VirtualFree"));
        InstanceKlass klass = ClassUtil.getKlass(VirtualFree.class);
        Method method = klass.findMethod("holder", "()I");
        MethodInjector.setNativePointer(method, code_base);

    }

    public synchronized static int invoke(long lpAddress, long dwSize, int dwFreeType) {
        Unsafe.putLong(code_base + 21, lpAddress);
        Unsafe.putLong(code_base + 31, dwSize);
        Unsafe.putInt(code_base + 41, dwFreeType);
        MethodInjector.dump(code_base, 54, System.out);
        return holder();
    }
}
