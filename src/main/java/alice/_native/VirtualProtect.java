package alice._native;

import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.Unsafe;

import java.nio.ByteBuffer;

public class VirtualProtect {
    @SuppressWarnings({"DuplicatedCode", "ReassignedVariable", "ConstantValue", "lossy-conversions", "UnusedAssignment"})
    private static void holder(){
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

    private static final long vp_code_base;

    static {
        for(int i = 0; i < 20000; i++){
            holder();
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
        vp_code_base = Shellcode.inject(payload, VirtualProtect.class,"holder","()V");
    }

    public synchronized static void invoke(long target, int size, int access){
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(target);
        byte[] addr = buffer.array();

        for (int i = 7, j = 14; i >= 0; i--, j++) {
            Unsafe.putByte(vp_code_base + j, addr[i]);
        }
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
        holder();
    }
}
