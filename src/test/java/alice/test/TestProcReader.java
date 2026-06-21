package alice.test;

import alice.util.ProcReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.ArrayList;
import java.util.Map;

import static alice.util.ProcReader.parseProcMaps;

public class TestProcReader {
    @Test
    @EnabledOnOs({OS.LINUX, OS.WINDOWS, OS.FREEBSD})
    public void test(){
        Map<String, ArrayList<ProcReader.MemoryMapping>> maps = parseProcMaps();
        for (ArrayList<ProcReader.MemoryMapping> lists : maps.values()) {
            for (ProcReader.MemoryMapping mapping : lists) {
                System.out.println(mapping);
            }
        }
    }
}
