package alice.util;

import alice.Platform;
import alice._native.linux.mmap;
import alice._native.win32.VirtualAlloc;
import sun.jvm.hotspot.types.Type;

import java.nio.charset.StandardCharsets;

import static alice.HSDB.typeDataBase;
import static alice.util.Constants.*;

public class MemoryUtil {
    public static String readSymbol(long symbolAddress) {
        Type symbolType = typeDataBase.lookupType("Symbol");
        long symbol = Unsafe.getAddress(symbolAddress);
        long body = symbol + symbolType.getField("_body").getOffset();
        int length = Unsafe.getShort(symbol + symbolType.getField("_length").getOffset()) & 0xffff;

        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            b[i] = Unsafe.getByte(body + i);
        }
        return new String(b, StandardCharsets.UTF_8);
    }

    public static long allocate(long size) {
        return Platform.win32 ? VirtualAlloc.invoke(0, size, MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE) : mmap.invoke(0, size, PROT_EXEC | PROT_WRITE | PROT_READ, MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
    }
}
