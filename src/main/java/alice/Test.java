package alice;

import java.util.PriorityQueue;

public class Test {

    static {
        //Init.ensureInit();
    }

    public static void main(String[] args) {
        PriorityQueue<Integer> test = new PriorityQueue<>();
        test.add(456);
        test.add(123);
        test.add(789);
        for (Integer i : test) {
            System.out.println(i.intValue());
        }
    }
}
