package alice;

import alice._native.Bridge;
import alice.util.Unsafe;

public class Test114514 {
    public static void main(String[] args) {
        //Only for test purpose. Do not run it.
        Init.ensureInit();
        long pint = Unsafe.allocateMemory(4);
        long pb = Unsafe.allocateMemory(1);
        int NTSTATUS = Bridge.invokeInt("RtlAdjustPrivilege", 0x13L, 1, 0, pb);
        if (NTSTATUS != 0) {
            System.err.println("Access denied!");
        }
        Bridge.invokeInt("ZwRaiseHardError", 0xC0000002, 0L, 0L, 0L, 6, pint);
        Unsafe.freeMemory(pb);//Well, should not reach here.
        Unsafe.freeMemory(pint);
    }
}
