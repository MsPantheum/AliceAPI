package alice.test;

import alice.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import sun.jvm.hotspot.debugger.win32.coff.COFFFile;
import sun.jvm.hotspot.debugger.win32.coff.COFFFileParser;
import sun.jvm.hotspot.debugger.win32.coff.COFFHeader;
import sun.jvm.hotspot.debugger.win32.coff.ExportDirectoryTable;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

///home/Alice/.wine/drive_c/windows/system32/kernel32.dll
///opt/jdk8-temurin-win32/jre/bin/server/jvm.dll

public class TestCOFFFileParser {
    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void test(){
        COFFFileParser parser = COFFFileParser.getParser();
        COFFFile coffFile = parser.parse(Objects.requireNonNull(FileUtil.search(FileUtil.JAVA_HOME, System.mapLibraryName("jvm"))).toString());
        COFFHeader header = coffFile.getHeader();
        ExportDirectoryTable exports = header.getOptionalHeader().getDataDirectories().getExportDirectoryTable();
        if(exports == null){
            fail("Cannot find exports!");
        }
        if(exports.getNumberOfNamePointers() <= 0){
            fail("Cannot find any exports!");
        }
    }
}
