package alice;

import alice.injector.MethodInjector;
import alice.util.AddressUtil;
import alice.util.ClassUtil;
import alice.util.Converter;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import java.io.PrintStream;

public class Test6 {
    public static void main(String[] args) throws Throwable {
        Init.ensureInit();
        Class<?> target_class = System.out.getClass();
        InstanceKlass klass = ClassUtil.getKlass(target_class);
        Method method = klass.findMethod("println", "(Ljava/lang/String;)V");
        String str = "Meow!!!";
        PrintStream out = System.out;
        long address1 = AddressUtil.getObjAddress(out);
        long address2 = AddressUtil.getObjAddress(str);
        long entry = MethodInjector.getInterpretedEntry(method);
        System.out.println("0x".concat(Long.toHexString(address1)));
        System.out.println("0x".concat(Long.toHexString(address2)));
        System.out.println("0x".concat(Long.toHexString(entry)));
        long tmp = Unsafe.allocateMemory(50);
        Unsafe.putByte(tmp, (byte) 0x58);
        Unsafe.putByte(tmp + 1, (byte) 0x48);
        Unsafe.putByte(tmp + 2, (byte) 0xb9); // movabs rcx, receiver
        Unsafe.putLong(tmp + 3, address1);
        Unsafe.putByte(tmp + 11, (byte) 0x51); // push rcx
        Unsafe.putByte(tmp + 12, (byte) 0x48);
        Unsafe.putByte(tmp + 13, (byte) 0xba); // movabs rdx, argument
        Unsafe.putLong(tmp + 14, address2);
        Unsafe.putByte(tmp + 22, (byte) 0x52); // push rdx
        Unsafe.putByte(tmp + 23, (byte) 0x50);
        Unsafe.putByte(tmp + 24, (byte) 0x48);
        Unsafe.putByte(tmp + 25, (byte) 0xbb);
        Unsafe.putLong(tmp + 26, Converter.getAddressValue(method.getAddress()));
        Unsafe.putByte(tmp + 34, (byte) 0x48);
        Unsafe.putByte(tmp + 35, (byte) 0xb8);
        Unsafe.putLong(tmp + 36, entry);
        Unsafe.putByte(tmp + 44, (byte) 0xff);
        Unsafe.putByte(tmp + 45, (byte) 0xe0);
        //MethodInjector.dump(tmp, 46, System.out);
        byte[] payload = Unsafe.readBytes(tmp, 46);
        MethodInjector.runShellcodeInterpreter(payload);
        System.out.println("Reach the end of main.");
    }
}
