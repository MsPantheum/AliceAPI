package alice.test;

import alice.util.ProcReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.LinkedList;
import java.util.Map;

import static alice.util.ProcReader.parseProcMaps;

public class TestProcReader {
    @Test
    @EnabledOnOs(OS.LINUX)
    public void test(){
        Map<String, LinkedList<ProcReader.MemoryMapping>> maps = parseProcMaps();
        for (LinkedList<ProcReader.MemoryMapping> lists : maps.values()) {
            for (ProcReader.MemoryMapping mapping : lists) {
                System.out.println(mapping);
            }
        }
    }
}
