package alice._native;

//WINBOOL WINAPI VirtualFree (LPVOID lpAddress, SIZE_T dwSize, DWORD dwFreeType);

import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.Unsafe;

public class VirtualFree {

    private static int holder() {
        System.getenv().keySet();
        return System.lineSeparator().length();
    }

    private static final long code_base;

    static {
        for (int i = 0; i < 20000; i++) {
            holder();
        }
        byte[] payload = new byte[39];

        payload[0] = (byte) 0x48;
        payload[1] = (byte) 0xb9;
        //lpAddress here
        payload[10] =  (byte) 0x48;
        payload[11] =  (byte) 0xba;
        //dwSize here
        payload[20] = (byte) 0x48;
        payload[21] =  (byte) 0xb8;
        //function
        payload[30] = (byte) 0x41;
        payload[31] = (byte) 0xb8;
        //dwFreeType here
        payload[36] = (byte) 0x48;
        payload[37] = (byte) 0xff;
        payload[38] = (byte) 0xe0;

        code_base = Unsafe.allocateMemory(39);
        Unsafe.writeBytes(code_base,payload);
        Unsafe.putLong(code_base + 22, SymbolLookup.lookup("VirtualFree"));
        VirtualProtect.invoke(code_base, 1, 0x40, 0);
        long address = Shellcode.getCompiledEntry(VirtualFree.class, "holder", "()I");
        assert InlineHook.simpleHook(address, code_base);
    }

    public static int invoke(long lpAddress,long dwSize,int dwFreeType) {
        Unsafe.putLong(code_base + 2,lpAddress);
        Unsafe.putLong(code_base + 12,dwSize);
        Unsafe.putLong(code_base + 32,dwFreeType);
        return holder();
    }
}
