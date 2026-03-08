package alice.injector;

import alice.util.Unsafe;
import sun.jvm.hotspot.types.Type;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static alice.HSDB.typeDataBase;

public class Dangerous {

    @SuppressWarnings({"DuplicatedCode", "ResultOfMethodCallIgnored", "ReassignedVariable", "ConstantValue", "lossy-conversions"})
    private static void mp(){
        for(int i = 9; i > 200; i++){
            i -= 1;
        }
        long lllll = 11221144L;
        int iii = 14514;
        while (iii != 0){
            iii--;
            lllll -= iii;
        }
        lllll++;
        int i = 0;
        int j = 123;
        while ( i < 1000) {
            i++;
            j--;
            if(j < 20){
                j+= 40;
            }

            double d = 114514.114514;
            d -= 0.1234;
            if(d < i){
                d += 3;
            }
        }
        i += 114514;
        while ( i < 123) {
            i--;
            j = 123;
            j++;
            if(j < 20){
                j+= 40;
            }

            double d = 114514.114514;
            d -= 0.1234;
            if(d < i){
                d += 1221;
            }
            d--;
        }
        double ddd= 12311.312312;
        long ll = 9923L;
        while (ddd > -9){
            ddd -= 1.223;
            i %= ddd;
            ll += j;
            j %= 12;
        }
        double dd = i + 14514;
        dd *= (i-32768);
        i ++;
        j+= (i*dd*ll);
        j -= lllll;
    }

    private static long mp_code_base;

    private static String getSymbol(long symbolAddress) {
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


    private static void inject(byte[] payload,Class<?> target,String name,String desc) {
        int oopSize = typeDataBase.lookupIntConstant("oopSize");
        long klassOffset = typeDataBase.lookupType("java_lang_Class").getCIntegerField("_klass_offset").getValue();
        long klass = oopSize == 8
                ? Unsafe.getLong(target, klassOffset)
                : Unsafe.getInt(target, klassOffset) & 0xffffffffL;

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

                long address = Unsafe.getAddress(method + _from_compiled_entry);
                mp_code_base = address;
                for(int j = 0; ;j++){
                    byte code = Unsafe.getByte(address + j);
                    if(code == (byte) 0xc2 || code == (byte) 0xc3){
                        if(j + 1 >= payload.length){
                            break;
                        } else {
                            throw new RuntimeException("Not enough space! Requiring " + payload.length + " but only have " + j +".");
                        }
                    }
                }
                for (int j = 0;j < payload.length ; j++) {
                    Unsafe.putByte(address + j, payload[j]);
                }

                break;

            }
        }


    }


    static {
        long func = SymbolLookup.lookup("/usr/lib/libc.so.6","mprotect@@GLIBC_2.2.5");
        byte[] payload = new byte[59];
        payload[0] = (byte) 0x55;
        payload[1] = (byte) 0x48;
        payload[2] = (byte) 0x89;
        payload[3] = (byte) 0xe5;
        payload[4] = (byte) 0x48;
        payload[5] = (byte) 0x83;
        payload[6] = (byte) 0xec;
        payload[7] = (byte) 0x10;
        payload[8] = (byte) 0x48;
        payload[9] = (byte) 0xb8;
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(func);
        byte[] addr = buffer.array();

        for(int i = 7,j = 10; i >=0; i--,j++){
            payload[j] = addr[i];
        }
        payload[18] = (byte) 0x48;
        payload[19] = (byte) 0x89;
        payload[20] = (byte) 0x45;
        payload[21] = (byte) 0xf8;
        payload[22] = (byte) 0x48;
        payload[23] = (byte) 0x8b;
        payload[24] = (byte) 0x45;
        payload[25] = (byte) 0xf8;
        payload[26] = (byte) 0x48;
        payload[27] = (byte) 0x89;
        payload[28] = (byte) 0x45;
        payload[29] = (byte) 0xf0;
        payload[30] = (byte) 0x48;
        payload[31] = (byte) 0xbf;

        payload[40] = (byte) 0xbe;
        payload[41] = (byte) 0x10;
        payload[42] = (byte) 0x00;
        payload[43] = (byte) 0x00;
        payload[44] = (byte) 0x00;
        payload[45] = (byte) 0xba;
        payload[46] = (byte) 0x07;
        payload[47] = (byte) 0x00;
        payload[48] = (byte) 0x00;
        payload[49] = (byte) 0x00;
        payload[50] = (byte) 0xff;
        payload[51] = (byte) 0x55;
        payload[52] = (byte) 0xf0;
        payload[53] = (byte) 0x48;
        payload[54] = (byte) 0x83;
        payload[55] = (byte) 0xc4;
        payload[56] = (byte) 0x10;
        payload[57] = (byte) 0x5d;
        payload[58] = (byte) 0xc3;

        for(int i = 0; i < 40000 ; i++){
            mp();
        }

        inject(payload, Dangerous.class,"mp","()V");
    }

    public static void mprotect(long target,int size){
        if(size % Unsafe.PAGE_SIZE != 0){
            throw new IllegalArgumentException("size must be a multiple of page size!");
        }
        target = target & -Unsafe.PAGE_SIZE;
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(target);
        byte[] addr = buffer.array();

        for(int i = 7,j = 32; i >=0; i--,j++){
            Unsafe.putByte(mp_code_base + j,addr[i]);
        }
        buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(size);
        addr = buffer.array();
        for(int i = 3,j = 41; i >=0; i--,j++){
            Unsafe.putByte(mp_code_base + j,addr[i]);
        }

        mp();
    }
}
