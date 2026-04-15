package alice;

import alice.util.ModuleUtil;
import alice.util.Unsafe;

public class TEST {

    private static class Clazz {
        private Object obj = null;
    }

    public static void main(String[] args) {
        ModuleUtil.openAll();
        Clazz meow = Unsafe.allocateInstance(Clazz.class);
        long offset = Unsafe.objectFieldOffset(Clazz.class, "obj");
        Unsafe.putObject(meow, offset, "114514191980");
        System.out.println((String) Unsafe.getObject(meow, offset));
    }

}
