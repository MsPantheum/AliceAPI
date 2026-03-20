package alice;

import alice._native.InlineHook;
import alice._native.system;
import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.FileUtil;
import alice.util.ProcessUtil;
import com.sun.jna.Function;
import com.sun.jna.Pointer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Test {

    private static void func1(){
        System.out.println("func1 called!");
    }

    private static void func2(){
        System.out.println("func2 called!");
    }

    private static void func3(){
        Runtime.getRuntime().runFinalization();
    }

    static {
        PrintStream backup = System.out;
        try {
            System.setOut(new PrintStream(new FileOutputStream(Platform.win32 ? "NUL" : "/dev/null")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 20000; i++) {
            func1();
            func2();
            func3();
        }
        System.setOut(backup);
    }

    public static void main(String[] args) throws Throwable {
        long func1 = Shellcode.getCompiledEntry(Test.class,"func1","()V");
        long func2 = Shellcode.getCompiledEntry(Test.class,"func2","()V");
        System.out.println("Hooking...");
        long trampoline = InlineHook.hookWithTrampoline(func1,func2);
        System.out.println("Hooked.");
        func1();
        System.out.println("State1");
        Shellcode.setCompiledEntry(Test.class,"func3","()V",trampoline);
        func3();
        ProcessUtil.exit(0);
    }
}
