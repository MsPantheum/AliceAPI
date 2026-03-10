package alice;

import alice.injector.SymbolLookup;
import alice.util.ProcessUtil;
import alice.util.Unsafe;
import com.sun.jna.NativeLibrary;
import com.sun.jna.ptr.IntByReference;
import sun.jvm.hotspot.types.Type;

import javax.swing.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static alice.HSDB.typeDataBase;

public class Test {

    @SuppressWarnings({"DuplicatedCode", "ReassignedVariable", "ConstantValue", "lossy-conversions", "UnusedAssignment"})
    private static void mp() {
        for (int i = 9; i > 200; i++) {
            i -= 1;
        }
//        for(double ddd = 1000222;ddd >= -99.2323242; ){
//            int i = (int) (ddd % 223);
//            ddd-=0.31332121;
//            ddd-=i;
//        }
        long lllll = 11221144L;
        int iii = 14514;
        while (iii != 0) {
            iii--;
            lllll -= iii;
        }
        lllll++;
        int i = 0;
        int j = 123;
        while (i < 1000) {
            i++;
            j--;
            if (j < 20) {
                j += 40;
            }

            double d = 114514.114514;
            d -= 0.1234;
            if (d < i) {
                d += 3;
            }
        }
        i += 114514;
        while (i < 123) {
            i--;
            j = 123;
            j++;
            if (j < 20) {
                j += 40;
            }

            double d = 114514.114514;
            d -= 0.1234;
            if (d < i) {
                d += 1221;
            }
            d--;
        }
        double ddd = 12311.312312;
        long ll = 9923L;
        while (ddd > -9) {
            ddd -= 1.223;
            i %= ddd;
            ll += j;
            j %= 12;
        }
        double dd = i + 14514;
        dd *= (i - 32768);
        i++;
        j += (i * dd * ll);
        j -= lllll;
    }

    private static long vp_code_base;

    private static String getSymbol(long symbolAddress) {
        mp();
        Type symbolType = typeDataBase.lookupType("Symbol");
        long symbol = Unsafe.getAddress(symbolAddress);
        long body = symbol + symbolType.getField("_body").getOffset();
        int length = Unsafe.getShort(symbol + symbolType.getField("_length").getOffset()) & 0xffff;

        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            b[i] = Unsafe.getByte(body + i);
        }
        return new String(b, StandardCharsets.UTF_8);
    }

    private static void inject(byte[] payload, Class<?> target, String name, String desc) {
        int oopSize = typeDataBase.lookupIntConstant("oopSize");
        long klassOffset = typeDataBase.lookupType("java_lang_Class").getCIntegerField("_klass_offset").getValue();
        long klass = oopSize == 8
                ? Unsafe.getLong(target, klassOffset)
                : Unsafe.getInt(target, klassOffset) & 0xffffffffL;

        System.out.println("Klass:" + Long.toHexString(klass));

        long methodArray = Unsafe.getAddress(klass + typeDataBase.lookupType("InstanceKlass").getField("_methods").getOffset());
        int methodCount = Unsafe.getInt(methodArray);
        long methods = methodArray + typeDataBase.lookupType("Array<Method*>").getAddressField("_data").getOffset();

        long constMethodOffset = typeDataBase.lookupType("Method").getAddressField("_constMethod").getOffset();
        Type constMethodType = typeDataBase.lookupType("ConstMethod");
        Type constantPoolType = typeDataBase.lookupType("ConstantPool");
        long constantPoolOffset = constMethodType.getAddressField("_constants").getOffset();
        long nameIndexOffset = constMethodType.getAddressField("_name_index").getOffset();
        long signatureIndexOffset = constMethodType.getCIntegerField("_signature_index").getOffset();
        long _from_compiled_entry = typeDataBase.lookupType("Method").getAddressField("_from_compiled_entry").getOffset();

        for (int i = 0; i < methodCount; i++) {
            long method = Unsafe.getAddress(methods + (long) i * oopSize);
            long constMethod = Unsafe.getAddress(method + constMethodOffset);

            long constantPool = Unsafe.getAddress(constMethod + constantPoolOffset);
            int nameIndex = Unsafe.getShort(constMethod + nameIndexOffset) & 0xffff;
            int signatureIndex = Unsafe.getShort(constMethod + signatureIndexOffset) & 0xffff;

            String _name = getSymbol(constantPool + constantPoolType.getSize() + (long) nameIndex * oopSize);
            String _desc = getSymbol(
                    constantPool + constantPoolType.getSize() + (long) signatureIndex * oopSize);
            if (name.equals(_name)
                    && desc.equals(_desc)) {
                System.out.println("Injecting...");
                System.out.println("Method:" + Long.toHexString(method));

                vp_code_base = Unsafe.getAddress(method + _from_compiled_entry);
                System.out.println("Compiled entry:" + Long.toHexString(method));
                for (int j = 0; ; j++) {
                    byte code = Unsafe.getByte(vp_code_base + j);
                    if (code == (byte) 0xc2 || code == (byte) 0xc3) {
                        if (j + 1 >= payload.length) {
                            break;
                        } else {
                            throw new RuntimeException("Not enough space! Requiring " + payload.length + " but only have " + j + ".");
                        }
                    }
                }

                for (int j = 0; j < payload.length; j++) {
                    Unsafe.putByte(vp_code_base + j, payload[j]);
                }
                break;

            }
        }


    }


    static {
        System.out.println("Pid:" + ProcessUtil.getPID());
        for (int i = 0; i < 20000; i++) {
            mp();
        }
        byte[] payload = new byte[60];
        payload[0] = (byte) 0x48;
        payload[1] = (byte) 0x83;
        payload[2] = (byte) 0xec;
        payload[3] = (byte) 0x28;

        payload[4] = (byte) 0xc7;
        payload[5] = (byte) 0x44;
        payload[6] = (byte) 0x24;
        payload[7] = (byte) 0x24;
        payload[8] = (byte) 0x00;
        payload[9] = (byte) 0x00;
        payload[10] = (byte) 0x00;
        payload[11] = (byte) 0x00;
        payload[12] = (byte) 0x48;
        payload[13] = (byte) 0xb9;
        //lpAddress here

        payload[22] = (byte) 0x4c;
        payload[23] = (byte) 0x8d;
        payload[24] = (byte) 0x4c;
        payload[25] = (byte) 0x24;
        payload[26] = (byte) 0x24;
        payload[27] = (byte) 0x48;
        payload[28] = (byte) 0xb8;
        long function = SymbolLookup.lookup("VirtualProtect");
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(function);
        byte[] addr = buffer.array();
        for (int i = 7, j = 29; i >= 0; i--, j++) {
            payload[j] = addr[i];
        }
        payload[37] = (byte) 0xba;
        //dwSize here
        payload[42] = (byte) 0x41;
        payload[43] = (byte) 0xb8;
        //flNewProtect here
        payload[48] = (byte) 0xff;
        payload[49] = (byte) 0xd0;
        payload[50] = (byte) 0xb8;
        payload[51] = (byte) 0x01;
        payload[52] = (byte) 0x00;
        payload[53] = (byte) 0x00;
        payload[54] = (byte) 0x00;
        payload[55] = (byte) 0x48;
        payload[56] = (byte) 0x83;
        payload[57] = (byte) 0xc4;
        payload[58] = (byte) 0x28;
        payload[59] = (byte) 0xc3;


        inject(payload, Test.class, "mp", "()V");
    }

    public static void mprotect(long target, int size, int access) {
//        if(size % Unsafe.PAGE_SIZE != 0){
//            throw new IllegalArgumentException("size must be a multiple of page size!");
//        }
        //target = target & -Unsafe.PAGE_SIZE;
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(target);
        byte[] addr = buffer.array();

        for (int i = 7, j = 14; i >= 0; i--, j++) {
            System.out.printf("%02x ", addr[i] & 0xFF);
            Unsafe.putByte(vp_code_base + j, addr[i]);
        }
        System.out.println();
        buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(size);
        addr = buffer.array();
        for (int i = 3, j = 38; i >= 0; i--, j++) {
            Unsafe.putByte(vp_code_base + j, addr[i]);
        }
        buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(access);
        addr = buffer.array();
        for (int i = 3, j = 44; i >= 0; i--, j++) {
            Unsafe.putByte(vp_code_base + j, addr[i]);
        }
        mp();
    }

    public static void main(String[] args) {

        System.out.println("PageSize:" + Unsafe.PAGE_SIZE);
        long jvm_sleep = SymbolLookup.lookup("JVM_Sleep");
        System.out.println("Target:" + Long.toHexString(jvm_sleep));
        System.out.println("VirtualProtect:" + Long.toHexString(SymbolLookup.lookup("VirtualProtect")));
        mprotect(jvm_sleep, 16, 0x40);
        System.out.println("----------------------------------------------------");
        for (int i = 0; i < 52; i++) {
            System.out.printf("%02x ", Unsafe.getByte(vp_code_base + i));
        }
        System.out.println("\n----------------------------------------------------");
        Unsafe.putByte(vp_code_base, (byte) 0xc3);
        //ProcessUtil.pause();

        System.out.println("---------");
        Unsafe.putByte(jvm_sleep, (byte) 0xc3);
        //mprotect(target,Unsafe.PAGE_SIZE,0x20);
        System.out.println("Try to sleep. Current time:" + System.nanoTime());
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Done. Current time:" + System.nanoTime());
    }
}
