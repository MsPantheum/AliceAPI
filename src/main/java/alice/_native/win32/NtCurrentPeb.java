package alice._native.win32;

import alice.injector.MethodInjector;
import alice.util.ClassUtil;
import alice.util.MemoryUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

public final class NtCurrentPeb {
    static {
        byte[] payload = new byte[10];
        int index = 0;
        payload[index++] = (byte) 0x65;
        payload[index++] = (byte) 0x48;
        payload[index++] = (byte) 0x8b;
        payload[index++] = (byte) 0x04;
        payload[index++] = (byte) 0x25;
        payload[index++] = (byte) 0x60;
        payload[index++] = (byte) 0x00;
        payload[index++] = (byte) 0x00;
        payload[index++] = (byte) 0x00;
        payload[index] = (byte) 0xc3;
        long code_base = MemoryUtil.allocate(10);
        Unsafe.writeBytes(code_base, payload);
        InstanceKlass klass = ClassUtil.getKlass(NtCurrentPeb.class);
        Method method = klass.findMethod("invoke", "()J");
        MethodInjector.setNativePointer(method, code_base);
    }

    public static synchronized native long invoke();
}
