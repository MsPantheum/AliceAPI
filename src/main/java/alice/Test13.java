package alice;

import alice._native.InvocationUtil;
import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;
import alice.util.Unsafe;

import java.io.IOException;
import java.util.Arrays;

public class Test13 {
    public static void main(String[] args) throws IOException {
        Init.ensureInit();
        long target = SymbolLookup.lookup("_ZL23jvm_define_class_commonP7JNIEnv_PKcP8_jobjectPKaiS4_S2_hP6Thread");
        System.out.println("target=0x".concat(Long.toHexString(target)));
        long start = SymbolLookup.lookup("JVM_DefineClass");
        InvocationUtil.FunctionInfo fi = InvocationUtil.getFunctionInfo(start);
        long size = fi.end - fi.start;
        System.out.println("size=0x".concat(Long.toHexString(size)));
        System.out.println("start=0x".concat(Long.toHexString(fi.start)));
        System.out.println("end=0x".concat(Long.toHexString(fi.end)));
        MethodInjector.dump(start, fi.end - fi.start, System.out);
        long _try;

        for (int i = 0; i < size; i++) {
            long current_position = start + i;
            byte opcode = Unsafe.getByte(current_position);
            if (opcode == (byte) 0xe8) {
                int offset = Unsafe.getInt(current_position + 1);
                _try = current_position + 5 + offset;
                if (_try >= fi.start && _try <= fi.end) {
                    continue;
                }
                if (Arrays.equals(Unsafe.readBytes(current_position + 9, 4), new byte[]{(byte) 0x48, (byte) 0x83, (byte) 0xc4, (byte) 0x20})) {
                    System.out.printf("0x%x\n", i);
                    System.out.println("_try=0x".concat(Long.toHexString(_try)));
                }
            }
        }
    }
}
