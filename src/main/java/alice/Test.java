package alice;

import alice.util.DebugUtil;

import java.util.PriorityQueue;

public class Test {

    static {
        //Init.ensureInit();
    }

    private static void test() {
        DebugUtil.printStackTrace();
    }

    public static void main(String[] args) {
        PriorityQueue<Integer> test = new PriorityQueue<>();
        test.add(456);
        test.add(123);
        test.add(789);
        System.out.println("1st");
        for (Integer i : test) {
            System.out.println(i.intValue());
        }
        System.out.println("2nd");
        for (Integer i : test) {
            System.out.println(i.intValue());
        }
        test();
    }
}
