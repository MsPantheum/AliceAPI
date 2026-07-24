package alice;

import alice._native.Bridge;
import alice._native.CString;
import alice.util.FileUtil;
import alice.util.Unsafe;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Test15 {
    public static void main(String[] args) throws IOException {
        Init.ensureInit();
        if (FileUtil.exists("output")) {
            FileUtil.write("output", new byte[0]);
        }
        System.setOut(new PrintStream(Files.newOutputStream(Paths.get("output"), StandardOpenOption.WRITE)));
        long results_pointer = Unsafe.allocateMemory(Long.BYTES * 1024);
        long cbNeeded_pointer = Unsafe.allocateMemory(Long.BYTES);
        int ret = Bridge.invokeInt("EnumDeviceDrivers", results_pointer, Long.BYTES * 1024, cbNeeded_pointer);
        if (ret != 0) {
            int cbNeeded = Unsafe.getInt(cbNeeded_pointer);
            int cDrivers = cbNeeded / Long.BYTES;
            System.out.println(cDrivers);
            long buffer = Unsafe.allocateMemory(1024);
            for (int i = 0; i < cDrivers; i++) {
                ret = Bridge.invokeInt("GetDeviceDriverBaseNameA", Unsafe.getLong(results_pointer + i * Long.BYTES), buffer, 1024);
                if (ret != 0) {
                    System.out.println(new CString(buffer));
                }
            }
        }
    }
}
