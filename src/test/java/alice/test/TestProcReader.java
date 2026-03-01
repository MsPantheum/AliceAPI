package alice.test;

import alice.util.ProcReader;
import alice.util.ProcessUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.List;

import static alice.util.ProcReader.parseProcMaps;

public class TestProcReader {
    @Test
    @EnabledOnOs(OS.LINUX)
    public void test(){
        List<ProcReader.MemoryMapping> maps = parseProcMaps(ProcessUtil.getPID());
        for (ProcReader.MemoryMapping map : maps) {
            System.out.println(map);
        }
    }
}
