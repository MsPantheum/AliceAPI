package alice.test;

import alice.Init;
import alice.Platform;
import alice.injector.NativeLibrary;
import alice.util.FileUtil;
import alice.util.ProcessUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Deprecated
public class TestNativeLibrary {

    static {
        Init.ensureInit();
    }

    @Test
    public void test(){
        assumeTrue(Platform.JAVA_VERSION <= 8);
        String libjvm = Objects.requireNonNull(FileUtil.search(FileUtil.JAVA_HOME, System.mapLibraryName("jvm"))).toString();
        NativeLibrary lib = NativeLibrary.load(libjvm,false);
        System.out.println(ProcessUtil.getPID());
        Assertions.assertNotNull(lib);
        long gHotSpotVMTypes = lib.find("gHotSpotVMTypes");
        assert gHotSpotVMTypes != 0;
    }
}
