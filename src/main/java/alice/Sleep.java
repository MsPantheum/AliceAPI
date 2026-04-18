package alice;

import alice.util.ProcessUtil;

public class Sleep {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(ProcessUtil.getPID());
        Thread.sleep(Long.MAX_VALUE);
    }
}
