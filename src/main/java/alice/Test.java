package alice;

import alice._native.jvmti.jvmtiInterface_1_.Allocate;
import alice.util.Unsafe;

public class Test {

    public static void meow() {
        System.out.println("Meow!");
    }

    public static void main(String[] args) {
        Init.ensureInit();
        long p = Unsafe.allocateMemory(Unsafe.ADDRESS_SIZE);
        Allocate.invoke(32, p);
        System.out.println("0x".concat(Long.toHexString(Unsafe.getLong(p))));
    }
}
