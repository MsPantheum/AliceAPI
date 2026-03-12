package alice._native;

import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.Unsafe;

import static alice.util.Constants.*;

//int munmap (void *__addr, size_t __len)

public class munmap {
    private static int holder() {
        System.getenv().keySet();
        return System.in.hashCode();
    }

    private static final long code_base;

    static {
        for (int i = 0; i < 20000; i++) {
            holder();
        }

        long function = SymbolLookup.lookup("munmap");
        assert function != 0;
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
        code_base = mmap.invoke(0, 32, PROT_READ | PROT_WRITE | PROT_EXEC, MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 22, function);
        long holder = Shellcode.getCompiledEntry(munmap.class, "holder", "()I");
        InlineHookSystemV.simpleHook(holder, code_base);
    }

    public static int invoke(long __addr, long __len) {
        Unsafe.putLong(code_base + 2, __addr);
        Unsafe.putLong(code_base + 12, __len);
        return holder();
    }

    public static void main(String[] args){
        long test = mmap.invoke(0, 8, PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
        Unsafe.putLong(test,0x114514191980L);
        System.out.println("0x"+Long.toHexString(Unsafe.getLong(test)));
        munmap.invoke(test,8);
        Runtime.getRuntime().exit(0);
    }
}
