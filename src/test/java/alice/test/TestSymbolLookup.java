package alice.test;

import alice.injector.NativeLibrary;
import alice.util.FileUtil;
import alice.util.ProcReader;
import alice.util.ProcessUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static alice.util.ProcReader.parseProcMaps;

public class TestSymbolLookup {
    @Test
    public void test() {
        System.out.println("Pid:"+ProcessUtil.getPID());
        String libjvm = FileUtil.search(FileUtil.getJavaHome(),System.mapLibraryName("jvm")).toString();
        long base = 0;
        List<ProcReader.MemoryMapping> maps = parseProcMaps(ProcessUtil.getPID());
        for (ProcReader.MemoryMapping map : maps) {
            if(map.pathname.equals(libjvm)){
                base = Long.parseLong(map.addressRangeStart,16);
                break;
            }
        }
        if(base == 0){
            throw new NoSuchElementException("Cannot get libjvm base address!");
        }
        System.out.println("Base:"+Long.toHexString(base));
        Map<String, ProcReader.SymbolInfo> symbols = ProcReader.readElf(libjvm);
        System.out.println(symbols.get("gHotSpotVMTypes"));
        long read1 = base + symbols.get("gHotSpotVMTypes").offset;
        long read2 = new NativeLibrary(libjvm,false).find("gHotSpotVMTypes");
        if(read2 == read1){
            System.out.println("Test passed.");
        } else throw new RuntimeException("Meow?");
    }
}
