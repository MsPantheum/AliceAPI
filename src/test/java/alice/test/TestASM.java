package alice.test;

import alice.injector.Dangerous;
import alice.injector.SymbolLookup;
import alice.util.FileUtil;
import alice.util.Unsafe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class TestASM {
    @Test
    @EnabledOnOs(OS.LINUX)
    public void test(){
        long target = SymbolLookup.lookup(FileUtil.search(FileUtil.getJavaHome(),System.mapLibraryName("jvm")).toString(),"JVM_Sleep");
        Dangerous.mprotect(target, Unsafe.PAGE_SIZE);
        Unsafe.putByte(target,(byte)0xc3);
        System.out.println("Try to sleep. Current time:" + System.nanoTime());
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Done. Current time:" + System.nanoTime());
    }
}
