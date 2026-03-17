package alice.test;

import alice._native.InlineHook;
import alice.injector.Shellcode;
import alice.util.ProcessUtil;
import org.junit.jupiter.api.Test;

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
        PrintStream backup = System.out;
        try {
            System.setOut(new PrintStream(new FileOutputStream("/dev/null")));
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
    public void test(){
        long func1 = Shellcode.getCompiledEntry(TestInlineHook4.class,"func1","()V");
        long func2 = Shellcode.getCompiledEntry(TestInlineHook4.class,"func2","()V");
        long trampoline = InlineHook.hookWithTrampoline(func1,func2);
        func1();
        Shellcode.setCompiledEntry(TestInlineHook4.class,"func3","()V",trampoline);
        //ProcessUtil.guiPause();
//        long meow = Shellcode.getCompiledEntry(TestInlineHook4.class,"func3","()V");
//        for(int i = 0; i < ){}
        func3();
        ProcessUtil.exit(0);
    }
}
