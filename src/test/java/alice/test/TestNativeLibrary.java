package alice.test;

import alice.injector.NativeLibrary;
import alice.util.FileUtil;
import alice.util.ProcessUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class TestNativeLibrary {

    @Test
    public void test(){
        String libjvm = Objects.requireNonNull(FileUtil.search(FileUtil.getJavaHome(), System.mapLibraryName("jvm"))).toString();
        NativeLibrary lib = NativeLibrary.load(libjvm,false);
        System.out.println(ProcessUtil.getPID());
        Assertions.assertNotNull(lib);
        long gHotSpotVMTypes = lib.find("gHotSpotVMTypes");
        assert gHotSpotVMTypes != 0;
    }
}
