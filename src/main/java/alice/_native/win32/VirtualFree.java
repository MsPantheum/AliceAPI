package alice._native.win32;

//WINBOOL WINAPI VirtualFree (LPVOID lpAddress, SIZE_T dwSize, DWORD dwFreeType);

import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.ClassUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

public final class VirtualFree {

    private static int holder() {
        return System.out.hashCode();
    }

    private static final long code_base;

    static {
        holder();
        byte[] payload = new byte[54];
        payload[0] = (byte) 0x48;
        payload[1] = (byte) 0x83;
        payload[2] = (byte) 0xec;
        payload[3] = (byte) 0x28;
        payload[4] = (byte) 0x48;
        payload[5] = (byte) 0xb8;
        //function
        payload[14] = (byte) 0x48;
        payload[15] = (byte) 0x89;
        payload[16] = (byte) 0x44;
        payload[17] = (byte) 0x24;
        payload[18] = (byte) 0x20;
        payload[19] = (byte) 0x48;
        payload[20] = (byte) 0xb9;
        //lpAddress here
        payload[29] = (byte) 0x48;
        payload[30] = (byte) 0xba;
        //dwSize here
        payload[39] = (byte) 0x41;
        payload[40] = (byte) 0xb8;
        //dwFreeType here
        payload[45] = (byte) 0xff;
        payload[46] = (byte) 0x54;
        payload[47] = (byte) 0x24;
        payload[48] = (byte) 0x20;
        payload[49] = (byte) 0x48;
        payload[50] = (byte) 0x83;
        payload[51] = (byte) 0xc4;
        payload[52] = (byte) 0x28;
        payload[53] = (byte) 0xc3;

        code_base = Unsafe.allocateMemory(payload.length);
        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 6, SymbolLookup.lookup("VirtualFree"));
        VirtualProtect.invoke(code_base, 1, 0x40, 0);
        InstanceKlass klass = ClassUtil.getKlass(VirtualFree.class);
        Method method = klass.findMethod("holder", "()I");
        Shellcode.setInterpretedEntry(method, code_base);
        Shellcode.antiOptimization(method);

    }

    public synchronized static int invoke(long lpAddress, long dwSize, int dwFreeType) {
        Unsafe.putLong(code_base + 21, lpAddress);
        Unsafe.putLong(code_base + 31, dwSize);
        Unsafe.putInt(code_base + 41, dwFreeType);
        Shellcode.dump(code_base, 54, System.out);
        return holder();
    }
}
