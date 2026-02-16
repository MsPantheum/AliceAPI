package alice.test;

import alice.util.ProcReader;
import alice.util.ProcessUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static alice.util.ProcReader.parseProcMaps;

public class TestProcReader {
    @Test
    public void test(){
        List<ProcReader.MemoryMapping> maps = parseProcMaps(ProcessUtil.getPID());
        for (ProcReader.MemoryMapping map : maps) {
            System.out.println(map);
        }
    }
}
