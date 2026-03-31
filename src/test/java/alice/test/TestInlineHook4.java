package alice.test;

import alice.Init;
import alice.Platform;
import alice._native.InlineHook;
import alice.injector.Shellcode;
import alice.util.ProcessUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class TestInlineHook4 {

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
        Init.ensureInit();
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

    @Test
    @EnabledOnOs(OS.LINUX) //I don't know why JUnit crashed on this.
    public void test(){
        long func1 = Shellcode.getCompiledEntry(TestInlineHook4.class,"func1","()V");
        long func2 = Shellcode.getCompiledEntry(TestInlineHook4.class,"func2","()V");
        long trampoline = InlineHook.hookWithTrampoline(func1,func2);
        func1();
        boolean success = Shellcode.setCompiledEntry(TestInlineHook4.class, "func3", "()V", trampoline);
        assert success;
        func3();
        ProcessUtil.exit(0);
    }
}
