package alice.test;

import alice.injector.NativeLibrary;
import alice.injector.SymbolLookup;
import alice.util.FileUtil;
import alice.util.ProcessUtil;
import org.junit.jupiter.api.Test;

public class TestSymbolLookup {
    @Test
    public void test() {
        System.out.println("Pid:"+ProcessUtil.getPID());
        String libjvm = FileUtil.search(FileUtil.getJavaHome(),System.mapLibraryName("jvm")).toString();
        long read1 = SymbolLookup.lookup("gHotSpotVMTypes");
        long read2 = NativeLibrary.load(libjvm,false).find("gHotSpotVMTypes");
        if(read2 == read1){
            System.out.println("Test passed.");
        } else throw new RuntimeException("Meow?");
    }
}
