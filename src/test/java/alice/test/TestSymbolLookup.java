package alice.test;

import alice.injector.NativeLibrary;
import alice.injector.SymbolLookup;
import alice.util.FileUtil;
import alice.util.ProcessUtil;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

public class TestSymbolLookup {
    @Test
    public void test() {
        System.out.println("Pid:"+ProcessUtil.getPID());
        String libjvm = FileUtil.search(FileUtil.getJavaHome(),System.mapLibraryName("jvm")).toString();
        long read1 = SymbolLookup.lookup("gHotSpotVMTypes");
        long read2 = Objects.requireNonNull(NativeLibrary.load(libjvm, false)).find("gHotSpotVMTypes");
        System.out.println(read1);
        System.out.println(read2);
        if(read2 == read1){
            System.out.println("Test passed.");
        } else {
            System.err.println(read1);
            System.err.println(read2);
            fail("Symbol not match!");
        }
    }
}
