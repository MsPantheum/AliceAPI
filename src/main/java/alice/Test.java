package alice;

import alice._native.win32.VirtualAlloc;
import alice.util.Unsafe;

import static alice.util.constants.Constants.*;

public class Test {
    public static void main(String[] args) {
        Init.ensureInit();
        long meow = VirtualAlloc.invoke(0, 8, MEM_COMMIT | MEM_RESERVE, PAGE_EXECUTE_READWRITE);
        System.out.println("0x".concat(Long.toHexString(meow)));
        Unsafe.putLong(meow, 0x114514191980L);
        System.out.println("0x".concat(Long.toHexString(Unsafe.getLong(meow))));
    }
}
