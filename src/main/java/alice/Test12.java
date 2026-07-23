package alice;

import alice._native.FunctionUtil;
import alice.injector.MethodInjector;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Test12 {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            return;
        }
        Init.ensureInit();
        FunctionUtil.FunctionInfo fi = FunctionUtil.getFunctionInfo(args[0]);
        System.out.println(fi.name);
        System.out.println(fi.start);
        System.out.println(fi.end);
        MethodInjector.dump(fi.start, fi.end - fi.start, new PrintStream(Files.newOutputStream(Paths.get("output"))));
    }
}
