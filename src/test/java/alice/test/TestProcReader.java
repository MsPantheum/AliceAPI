package alice.test;

import alice.util.ProcReader;
import alice.util.ProcessUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.Map;

import static alice.util.ProcReader.parseProcMaps;

public class TestProcReader {
    @Test
    @EnabledOnOs(OS.LINUX)
    public void test(){
        Map<String,ProcReader.MemoryMapping> maps = parseProcMaps();
        for (ProcReader.MemoryMapping map : maps.values()) {
            System.out.println(map);
        }
    }
}
