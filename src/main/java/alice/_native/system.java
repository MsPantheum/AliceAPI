package alice._native;

import alice.Platform;
import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.AddressUtil;
import alice.util.CString;
import alice.util.Unsafe;

//int __cdecl system(const char *_Command);

import static alice.util.Constants.*;

public class system {

    private static int holder() {
        System.getenv().keySet();
        return System.lineSeparator().length();
    }

    private static final long code_base;

    static {
        for (int i = 0; i < 20000; i++) {
            holder();
        }
        byte[] payload = new byte[23];
        payload[0] = (byte) 0x48;
        payload[1] = (byte) 0xb9;
        //_Command here
        payload[10] = (byte) 0x48;
        payload[11] = (byte) 0xb8;
        //function
        payload[20] = (byte) 0x48;
        payload[21] = (byte) 0xff;
        payload[22] = (byte) 0xe0;
        code_base = Platform.win32 ? VirtualAlloc.invoke(0, 23, MEM_COMMIT | MEM_RESERVE, 0x40) : mprotect.invoke(0, 23, PROT_EXEC | PROT_WRITE | PROT_READ);
        assert code_base != 0;
        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 12, SymbolLookup.lookup("system"));
        long address = Shellcode.getCompiledEntry(system.class, "holder", "()I");
        boolean success = InlineHook.simpleHook(address, code_base);
        assert success;
    }

    public static synchronized int invoke(String cmd) {
        CString cstr = CString.create(cmd);
        Unsafe.putLong(code_base + 2, cstr.getAddress());
        int ret = holder();
        cstr.release();
        return ret;
    }
}
