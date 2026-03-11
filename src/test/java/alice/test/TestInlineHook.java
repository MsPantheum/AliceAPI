package alice.test;

import alice._native.InlineHook;
import alice.injector.Shellcode;
import alice.util.ProcessUtil;
import org.junit.jupiter.api.Test;

public class TestInlineHook {

    private static int test_value = 0x0;

    static {
        for(int i = 0; i < 40000; i++){
            func1(0x114514);
        }
        for(int i = 0; i < 40000; i++){
            func2(0x191980);
        }
    }

    @SuppressWarnings({"DuplicatedCode", "ReassignedVariable", "ConstantValue", "lossy-conversions", "UnusedAssignment"})
    private static void func1(int value){
        test_value = value + 1;
        for(int i = 9; i > 200; i++){
            i -= 1;
        }
        long lllll = 11221144L;
        int iii = 14514;
        while (iii != 0){
            iii--;
            lllll -= iii;
        }
        lllll++;
        int i = 0;
        int j = 123;
        while ( i < 1000) {
            i++;
            j--;
            if(j < 20){
                j+= 40;
            }

            double d = 114514.114514;
            d -= 0.1234;
            if(d < i){
                d += 3;
            }
        }
        i += 114514;
        while ( i < 123) {
            i--;
            j = 123;
            j++;
            if(j < 20){
                j+= 40;
            }

            double d = 114514.114514;
            d -= 0.1234;
            if(d < i){
                d += 1221;
            }
            d--;
        }
        double ddd= 12311.312312;
        long ll = 9923L;
        while (ddd > -9){
            ddd -= 1.223;
            i %= ddd;
            ll += j;
            j %= 12;
        }
        double dd = i + 14514;
        dd *= (i-32768);
        i ++;
        j+= (i*dd*ll);
        j -= lllll;
    }//test_value = 0x114514

    @SuppressWarnings({"DuplicatedCode", "ReassignedVariable", "ConstantValue", "lossy-conversions", "UnusedAssignment"})
    private static void func2(int value){
        test_value = value - 1;
        for(int i = 9; i > 200; i++){
            i -= 1;
        }
        long lllll = 11221144L;
        int iii = 14514;
        while (iii != 0){
            iii--;
            lllll -= iii;
        }
        lllll++;
        int i = 0;
        int j = 123;
        while ( i < 1000) {
            i++;
            j--;
            if(j < 20){
                j+= 40;
            }

            double d = 114514.114514;
            d -= 0.1234;
            if(d < i){
                d += 3;
            }
        }
        i += 114514;
        while ( i < 123) {
            i--;
            j = 123;
            j++;
            if(j < 20){
                j+= 40;
            }

            double d = 114514.114514;
            d -= 0.1234;
            if(d < i){
                d += 1221;
            }
            d--;
        }
        double ddd= 12311.312312;
        long ll = 9923L;
        while (ddd > -9){
            ddd -= 1.223;
            i %= ddd;
            ll += j;
            j %= 12;
        }
        double dd = i + 14514;
        dd *= (i-32768);
        i ++;
        j+= (i*dd*ll);
        j -= lllll;
    }//test_value = 0x191980

    @Test
    public void test(){
        System.out.println("Pid:"+ProcessUtil.getPID());
        func1(0x114514); System.out.println("After func1 called: 0x" + Long.toHexString(test_value));
        assert test_value == 0x114514 + 1;
        func2(0x191980); System.out.println("After func2 called: 0x" + Long.toHexString(test_value));
        assert test_value == 0x191980 - 1;
        long p1 = Shellcode.getCompiledEntry(TestInlineHook.class,"func1","(I)V");
        long p2 = Shellcode.getCompiledEntry(TestInlineHook.class,"func2","(I)V");
        ProcessUtil.guiPause();
        InlineHook.hook(p2,p1);
        func2(0x12345);
        System.out.println("After func2 called again: 0x"+Long.toHexString(test_value));
        System.out.println("Success:" + (test_value == (0x12345 + 1)));
    }
}
