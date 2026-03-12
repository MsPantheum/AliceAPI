package alice.test;

import alice._native.InlineHookSystemV;
import alice._native.mprotect;
import alice.injector.Shellcode;
import alice.util.AddressUtil;
import alice.util.Unsafe;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class TestInlineHook2 {

    @SuppressWarnings({"DuplicatedCode", "ReassignedVariable", "ConstantValue", "lossy-conversions", "UnusedAssignment"})
    private static void func() {
        for (int i = 9; i > 200; i++) {
            i -= 1;
        }
        long lllll = 11221144L;
        int iii = 14514;
        while (iii != 0) {
            iii--;
            lllll -= iii;
        }
        lllll++;
        int i = 0;
        int j = 123;
        while (i < 1000) {
            i++;
            j--;
            if (j < 20) {
                j += 40;
            }

            double d = 114514.114514;
            d -= 0.1234;
            if (d < i) {
                d += 3;
            }
        }
        i += 114514;
        while (i < 123) {
            i--;
            j = 123;
            j++;
            if (j < 20) {
                j += 40;
            }

            double d = 114514.114514;
            d -= 0.1234;
            if (d < i) {
                d += 1221;
            }
            d--;
        }
        double ddd = 12311.312312;
        long ll = 9923L;
        while (ddd > -9) {
            ddd -= 1.223;
            i %= ddd;
            ll += j;
            j %= 12;
        }
        double dd = i + 14514;
        dd *= (i - 32768);
        i++;
        j += (i * dd * ll);
        j -= lllll;
    }

    private static void hello(){
        System.out.println("Hello World!");
    }

    static {
        for (int i = 0; i < 20000; i++) {
            func();
        }
        PrintStream backup = System.out;
        try {
            System.setOut(new PrintStream(new FileOutputStream("/dev/null")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 20000; i++) {
            hello();
        }
        System.setOut(backup);
    }

    @Test
    public void test() {
        byte[] payload = new byte[]{(byte)0xb8,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0xbf,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x48,(byte)0x8d,(byte)0x35,(byte)0xef,(byte)0x0f,(byte)0x00,(byte)0x00,(byte)0xba,(byte)0x0e,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0f,(byte)0x05,(byte)0xb8,(byte)0x3c,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x48,(byte)0x31,(byte)0xff,(byte)0x0f,(byte)0x05};
        System.out.println(payload.length);
        long neo = Unsafe.allocateMemory(payload.length);
        Unsafe.writeBytes(neo, payload);
        //long neo = Shellcode.getCompiledEntry(TestInlineHook2.class,"hello","()V");
        int success = mprotect.invoke(AddressUtil.align(neo),1,0x1 | 0x2 | 0x4);
        assert success == 0;
        long ori = Shellcode.getCompiledEntry(TestInlineHook2.class,"func","()V");
        InlineHookSystemV.simpleHook(ori,neo);
        func();
    }
}
