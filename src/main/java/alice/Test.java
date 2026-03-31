package alice;

import alice.util.AddressUtil;
import alice.util.ClassUtil;
import alice.util.ProcessUtil;
import sun.jvm.hotspot.oops.InstanceKlass;

public class Test {

    public static void test() {
        new Object();
    }

    static {
        Init.ensureInit();
    }

    public static void main(String[] args) {
        InstanceKlass klass = ClassUtil.getKlass(Init.class);
        long address = AddressUtil.getAddressValue(klass);
        AddressUtil.println(address);
        System.out.println("Start 1st test.");
        long time = System.nanoTime();
        boolean result = AddressUtil.safeAddress(address);
        time = System.nanoTime() - time;
        System.out.println("Result:" + result);
        System.out.println("Time:" + time);
        System.out.println("End.");
        System.out.println("Start 2st test.");
        time = System.nanoTime();
        result = AddressUtil.safeAddress("libjvm.so", address);
        time = System.nanoTime() - time;
        System.out.println("Result:" + result);
        System.out.println("Time:" + time);
        System.out.println("End.");
        ProcessUtil.pause();
    }

}
