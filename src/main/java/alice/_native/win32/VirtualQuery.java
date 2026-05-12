package alice._native.win32;

//SIZE_T WINAPI VirtualFree (LPVOID lpAddress, PMEMORY_BASIC_INFORMATION lpBuffer, SIZE_T dwLength);

import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.ClassUtil;
import alice.util.MemoryUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

public class VirtualQuery {

    private static long holder() {
        return System.nanoTime();
    }

    private static final long code_base;

    static {
        holder();
        byte[] payload = new byte[64];
        payload[0] = (byte) 0x55;
        payload[1] = (byte) 0x48;
        payload[2] = (byte) 0x89;
        payload[3] = (byte) 0xe5;
        payload[4] = (byte) 0x48;
        payload[5] = (byte) 0x83;
        payload[6] = (byte) 0xec;
        payload[7] = (byte) 0x30;
        payload[8] = (byte) 0x48;
        payload[9] = (byte) 0xb8;
        //function here
        payload[18] = (byte) 0x48;
        payload[19] = (byte) 0x89;
        payload[20] = (byte) 0x45;
        payload[21] = (byte) 0xf8;
        payload[22] = (byte) 0x49;
        payload[23] = (byte) 0xb8;
        //dwLength here
        payload[32] = (byte) 0x48;
        payload[33] = (byte) 0xba;
        //lpBuffer here
        payload[42] = (byte) 0x48;
        payload[43] = (byte) 0xb9;
        //lpAddress here
        payload[52] = (byte) 0x48;
        payload[53] = (byte) 0x8b;
        payload[54] = (byte) 0x45;
        payload[55] = (byte) 0xf8;
        payload[56] = (byte) 0xff;
        payload[57] = (byte) 0xd0;
        payload[58] = (byte) 0x48;
        payload[59] = (byte) 0x83;
        payload[60] = (byte) 0xc4;
        payload[61] = (byte) 0x30;
        payload[62] = (byte) 0x5d;
        payload[63] = (byte) 0xc3;
        code_base = MemoryUtil.allocate(payload.length);
        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 10, SymbolLookup.lookup("VirtualQuery"));
        InstanceKlass klass = ClassUtil.getKlass(VirtualQuery.class);
        Method method = klass.findMethod("holder", "()J");
        Shellcode.setInterpretedEntry(method, code_base);
        Shellcode.antiOptimization(method);
    }

    public static synchronized long invoke(long lpAddress, long lpBuffer, long dwLength) {
        Unsafe.putLong(code_base + 44, lpAddress);
        Unsafe.putLong(code_base + 34, lpBuffer);
        Unsafe.putLong(code_base + 24, dwLength);
        return holder();
    }
}
