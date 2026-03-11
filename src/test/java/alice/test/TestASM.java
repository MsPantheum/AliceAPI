package alice.test;

import alice.Platform;
import alice._native.VirtualProtect;
import alice._native.mprotect;
import alice.injector.SymbolLookup;
import alice.util.FileUtil;
import alice.util.Unsafe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.Objects;

public class TestASM {
    @Test
    @EnabledOnOs(value = {OS.LINUX,OS.WINDOWS})
    public void test(){
        long target = SymbolLookup.lookup(Objects.requireNonNull(FileUtil.search(FileUtil.getJavaHome(), System.mapLibraryName("jvm"))).toString(),"JVM_Sleep");
        if(!Platform.win32){
            mprotect.invoke(target, Unsafe.PAGE_SIZE);
        } else {
            VirtualProtect.invoke(target,1,0x40);
        }
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
