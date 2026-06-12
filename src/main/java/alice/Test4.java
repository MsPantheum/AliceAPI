package alice;

import alice.injector.MethodInjector;
import alice.util.ClassUtil;
import alice.util.Converter;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

public class Test4 {
    private static void test() {
        System.out.println("test called!");
    }

    public static void main(String[] args) {
        //test();
        Init.ensureInit();
        InstanceKlass klass = ClassUtil.getKlass(Test4.class);
        Method method = klass.findMethod("test", "()V");
        long methodAddress = Converter.getAddressValue(method.getAddress());
        long interpretedEntry = MethodInjector.getInterpretedEntry(method);
        long tmp = Unsafe.allocateMemory(22);
        Unsafe.putByte(tmp, (byte) 0x48);
        Unsafe.putByte(tmp + 1, (byte) 0xbb); // movabs rbx, Method*
        Unsafe.putLong(tmp + 2, methodAddress);
        Unsafe.putByte(tmp + 10, (byte) 0x48);
        Unsafe.putByte(tmp + 11, (byte) 0xb8); // movabs rax, interpreted entry
        Unsafe.putLong(tmp + 12, interpretedEntry);
        Unsafe.putByte(tmp + 20, (byte) 0xff);
        Unsafe.putByte(tmp + 21, (byte) 0xe0); // jmp rax
        byte[] payload = Unsafe.readBytes(tmp, 22);
        MethodInjector.runShellcodeInterpreter(payload);
    }
}
