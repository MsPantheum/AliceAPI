package alice.test;

import alice.Init;
import alice.injector.SymbolLookup;
import alice.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.Objects;

public class TestASM {
    @Test
    @EnabledOnOs(value = {OS.LINUX, OS.WINDOWS})
    public void test() {
        Init.ensureInit();
        System.out.println("Pid:" + ProcessUtil.getPID());
        long target = SymbolLookup.lookup(Objects.requireNonNull(FileUtil.search(FileUtil.JAVA_HOME, System.mapLibraryName("jvm"))).toString(), "JVM_Sleep");
        System.out.println("Target:0x" + Long.toHexString(target));
        MemoryUtil.setMemoryRWX(target,1);
        Unsafe.putByte(target, (byte) 0xc3);
        System.out.println("Try to sleep. Current time:" + System.nanoTime());
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Done. Current time:" + System.nanoTime());
    }
}
