package alice;

import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;
import alice.util.Converter;
import alice.util.MemoryUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.types.TypeDataBase;

import java.util.Arrays;

public class Test3 {
    public static void main(String[] args) {
        Init.ensureInit();
        long true_target = Unsafe.getLong(SymbolLookup.lookup("libjvm.so", "_ZN12StubRoutines16_call_stub_entryE"));
        TypeDataBase dataBase = HSDB.typeDataBase;
        System.out.println("Meow?");
        long meow = Converter.getAddressValue(dataBase.lookupType("StubRoutines").getAddressField("_call_stub_return_address").getValue());
        System.out.println("Meow!");
        byte[] sample1 = new byte[]{(byte) 0x55, (byte) 0x48, (byte) 0x89, (byte) 0xe5};
        byte[] sample2 = new byte[]{(byte) 0x55, (byte) 0x48, (byte) 0x8b, (byte) 0xec};
        System.out.println("True target:".concat(String.valueOf(true_target)));
        System.out.println("_call_stub_return_address:".concat(String.valueOf(meow)));
        for (int i = 0; ; i++) {
            long test = meow - i;
            if (!MemoryUtil.readable(test)) {
                MethodInjector.dump(test + 1, i, System.out);
                break;
            }
            //System.out.println("Test:".concat(String.valueOf(test)));
            byte[] dump = Unsafe.readBytes(test, 4);
            if (Arrays.equals(dump, sample1) || Arrays.equals(dump, sample2)) {
                System.out.println(test);
                break;
            }
        }
    }
}
