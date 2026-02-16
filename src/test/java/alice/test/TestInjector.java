package alice.test;

import alice.injector.NativeLibrary;
import alice.util.FileUtil;
import alice.util.ProcessUtil;
import org.junit.jupiter.api.Test;

import javax.swing.*;

public class TestInjector {

    @Test
    public void test(){
        String libjvm = FileUtil.search(FileUtil.getJavaHome(),System.mapLibraryName("jvm")).toString();
        NativeLibrary lib = NativeLibrary.load(libjvm,false);
        System.out.println(ProcessUtil.getPID());
        long gHotSpotVMTypes = lib.find("gHotSpotVMTypes");
        assert gHotSpotVMTypes != 0;
        System.out.println(gHotSpotVMTypes);
    }
}
