package alice;

import alice.util.DebugUtil;
import sun.jvm.hotspot.types.Type;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class Test1 {
    public static void main(String[] args) throws IOException {
        Init.ensureInit();
        PrintStream out = System.out;
        System.setOut(new PrintStream(Files.newOutputStream(Paths.get("output"))));
        Iterator<Type> iterator = HSDB.typeDataBase.getTypes();
        iterator.forEachRemaining(DebugUtil::printType);
        out.println(HSDB.typeDataBase.lookupIntConstant("InstanceKlass::initialization_error"));
    }
}
