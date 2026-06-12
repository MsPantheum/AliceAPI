package alice;

import alice.util.ClassUtil;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

public class Test {

    public static void meow() {
        System.out.println("Meow!");
    }

    public static void main(String[] args) {
        Init.ensureInit();
        InstanceKlass klass = ClassUtil.getKlass(Test.class);
        Method method = klass.findMethod("meow", "()V");

    }
}
