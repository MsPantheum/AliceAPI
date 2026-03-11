package alice._native;

import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.Unsafe;

import java.nio.ByteBuffer;

public class mprotect {

    @SuppressWarnings({"DuplicatedCode", "ReassignedVariable", "ConstantValue", "lossy-conversions", "UnusedAssignment"})
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

    private static final long mp_code_base;

    static {
        System.out.println("Setting up mprotect call payload.");
        long func = SymbolLookup.lookup("mprotect");
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

        System.out.println("Forcing C2 to optimize the target...");
        for(int i = 0; i < 40000 ; i++){
            mp();
        }
        System.out.println("Done.");
        System.out.println("Injecting payload.");
        mp_code_base = Shellcode.inject(payload, mprotect.class,"mp","()V");
        System.out.println("Done.");
    }

    public static void invoke(long target, int size){
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
