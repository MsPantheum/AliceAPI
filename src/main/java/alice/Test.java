package alice;

import alice._native.linux.mprotect;
import alice._native.win32.VirtualProtect;
import alice.injector.SymbolLookup;
import alice.util.AddressUtil;
import alice.util.FileUtil;
import alice.util.ProcessUtil;
import alice.util.Unsafe;

import java.util.Objects;

import static alice.util.constants.Constants.*;

public class Test {

    public static void main(String[] args) throws Throwable {
        System.out.println("Start!");
        Init.ensureInit();
        System.out.println("Pid:" + ProcessUtil.getPID());
        long target = SymbolLookup.lookup(Objects.requireNonNull(FileUtil.search(FileUtil.JAVA_HOME, System.mapLibraryName("jvm"))).toString(), "JVM_Sleep");
        System.out.println("Target:0x" + Long.toHexString(target));
        if (!Platform.win32) {
            int success = mprotect.invoke(AddressUtil.align(target), 1, PROT_READ | PROT_WRITE | PROT_EXEC);
            assert success == 0;
        } else {
            VirtualProtect.invoke(target, 1, 0x40, 0);
        }
        Unsafe.putByte(target, (byte) 0xc3);
        System.out.println("Try to sleep. Current time:" + System.nanoTime());
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Done. Current time:" + System.nanoTime());
        Runtime.getRuntime().exit(0);
    }
}
