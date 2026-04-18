package alice._native.linux;

import alice._native.InlineHook;
import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.MemoryUtil;
import alice.util.Unsafe;

//int munmap (void *__addr, size_t __len)

public final class munmap {

    private static int holder() {
        long lllll = 11221144L;
        int iii = 14514;
        while (iii != 0 && lllll > 0){
            iii--;
            lllll -= iii;
        }
        return System.in.hashCode();
    }

    private static final long code_base;

    static {
        for (int i = 0; i < 20000; i++) {
            holder();
        }
        byte[] payload = new byte[32];
        payload[0] = (byte) 0x48;
        payload[1] = (byte) 0xbf;
        //__addr here
        payload[10] = (byte) 0x48;
        payload[11] = (byte) 0xbe;
        //__len here
        payload[20] = (byte) 0x48;
        payload[21] = (byte) 0xb8;
        //function
        payload[30] = (byte) 0xff;
        payload[31] = (byte) 0xe0;
        code_base = MemoryUtil.allocate(32);
        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 22, SymbolLookup.lookup("munmap"));
        long holder = Shellcode.getCompiledEntry(munmap.class, "holder", "()I");
        InlineHook.simpleHook(holder, code_base);
    }

    public synchronized static int invoke(long __addr, long __len) {
        Unsafe.putLong(code_base + 2, __addr);
        Unsafe.putLong(code_base + 12, __len);
        return holder();
    }

}
