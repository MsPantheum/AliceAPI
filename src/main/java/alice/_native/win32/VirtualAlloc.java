package alice._native.win32;

//LPVOID WINAPI VirtualAlloc (LPVOID lpAddress, SIZE_T dwSize, DWORD flAllocationType, DWORD flProtect);

import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.ClassUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

public final class VirtualAlloc {

    @SuppressWarnings({"DuplicatedCode"})
    public static long holder() {
        return System.nanoTime();
    }

    private static final long code_base;

    static {
        holder();
        byte[] payload = new byte[60];
        payload[0] = (byte) 0x48;
        payload[1] = (byte) 0x83;
        payload[2] = (byte) 0xec;
        payload[3] = (byte) 0x28;
        payload[4] = (byte) 0x48;
        payload[5] = (byte) 0xb8;
        //function here
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
        //flAllocationType here
        payload[45] = (byte) 0x41;
        payload[46] = (byte) 0xb9;
        //flProtect here
        payload[51] = (byte) 0xff;
        payload[52] = (byte) 0x54;
        payload[53] = (byte) 0x24;
        payload[54] = (byte) 0x20;
        payload[55] = (byte) 0x48;
        payload[56] = (byte) 0x83;
        payload[57] = (byte) 0xc4;
        payload[58] = (byte) 0x28;
        payload[59] = (byte) 0xc3;

        code_base = Unsafe.allocateMemory(payload.length);
        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 6, SymbolLookup.lookup("VirtualAlloc"));
        InstanceKlass klass = ClassUtil.getKlass(VirtualAlloc.class);
        Method method = klass.findMethod("holder", "()J");
        Shellcode.antiOptimization(method);
        Shellcode.setInterpretedEntry(method, code_base);
        int success = VirtualProtect.invoke(code_base, 1, 0x40, 0);
        if (success == 0) {
            throw new IllegalStateException();
        }
    }

    public static synchronized long invoke(long lpAddress, long dwSize, int flAllocationType, int flProtect) {
        Unsafe.putLong(code_base + 21, lpAddress);
        Unsafe.putLong(code_base + 31, dwSize);
        Unsafe.putInt(code_base + 41, flAllocationType);
        Unsafe.putInt(code_base + 47, flProtect);
        return holder();
    }
}
