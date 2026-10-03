package alice;

import alice._native.InvocationUtil;
import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Test7 {
    public static void main(String[] args) throws IOException {
        Init.ensureInit();
        String target = args[0];
        String length_raw = args.length > 1 ? args[1] : null;
        long length = length_raw != null ? (length_raw.startsWith("0x") ? Long.parseLong(length_raw, 16) : Long.parseLong(length_raw)) : (InvocationUtil.getFunctionInfo(target).end - InvocationUtil.getFunctionInfo(target).start);
        MethodInjector.dump(SymbolLookup.lookupValue(target), length, new PrintStream(Files.newOutputStream(Paths.get("dump_results"))));
    }
}
