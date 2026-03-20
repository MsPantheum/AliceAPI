package alice.test;

import alice._native.InlineHook;
import alice.injector.Shellcode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

@SuppressWarnings({"rawtypes", "unchecked"})
public class TestInlineHook3 {

    @SuppressWarnings({"UnusedReturnValue", "unused", "SameParameterValue"})
    private static boolean fuck(ArrayList _this, Object obj){
        return false;
    }

    static {
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") ArrayList tmp = new ArrayList(1);
        for(int i = 0;i < 20000;i ++){
            //noinspection ResultOfMethodCallIgnored
            fuck(null,null);
            tmp.add(null);
            tmp.clear();
        }
    }

    @Test
    @Disabled
    public void test(){
        Class<?> clazz = ArrayList.class;
        assert clazz.getClassLoader() == null;
        long ori = Shellcode.getCompiledEntry(ArrayList.class,"add","(Ljava/lang/Object;)Z");
        assert ori != 0;
        long neo = Shellcode.getCompiledEntry(TestInlineHook3.class,"fuck","(Ljava/util/ArrayList;Ljava/lang/Object;)Z");
        assert neo != 0;
        InlineHook.simpleHook(ori,neo);
        //Well,JUnit breaks after this. It ignores this test and not execute subsequent instructions.
        System.out.println("Testing...");
        ArrayList list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        System.out.println("Size:"+list.size());
    }
}
