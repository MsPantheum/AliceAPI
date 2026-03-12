package alice._native;

//int mprotect (void *__addr, size_t __len, int __prot)

import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.Unsafe;

import java.nio.ByteBuffer;

public class mprotect {
    @SuppressWarnings({"DuplicatedCode", "ReassignedVariable", "ConstantValue", "lossy-conversions", "UnusedAssignment"})
    private static int mp(){
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
        return j;
    }

    private static final long mp_code_base;

    static {
        System.out.println("Forcing C2 to optimize the target...");
        for(int i = 0; i < 20000 ; i++){
            mp();
        }
        System.out.println("Done.");

        System.out.println("Setting up mprotect call payload.");
        long func = SymbolLookup.lookup("mprotect");
        byte[] payload = new byte[37];
        payload[0] = (byte) 0x48;
        payload[1] = (byte) 0xbf;
        //__addr here
        payload[10] = (byte) 0x48;
        payload[11] = (byte) 0xbe;
        //__len here
        payload[20] = (byte) 0x48;
        payload[21] = (byte) 0xb8;
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(func);
        byte[] addr = buffer.array();

        for(int i = 7,j = 22; i >=0; i--,j++){
            payload[j] = addr[i];
        }
        payload[30] = (byte) 0xba;
        //__prot here
        payload[35] = (byte) 0xff;
        payload[36] = (byte) 0xe0;

        System.out.println("Injecting payload.");
        long tmp = Shellcode.inject(payload, mprotect.class,"mp","()I");
        assert tmp != 0;
        mp_code_base = tmp;

        System.out.println("Done.");
    }

    public static int invoke(long addr,long size,int prot){
        System.out.println("final_target=0x" +  Long.toHexString(addr));
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(addr);
        byte[] data = buffer.array();
        System.out.println("---1");
        for(int i = 7,j = 2; i >=0; i--,j++){
            Unsafe.putByte(mp_code_base + j, data[i]);
        }
        buffer.clear();
        buffer.putLong(size);
        data = buffer.array();
        System.out.println("---2");
        for(int i = 7,j = 12; i >=0; i--,j++){
            Unsafe.putByte(mp_code_base + j, data[i]);
        }
        buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(prot);
        data = buffer.array();
        System.out.println("---3");
        for(int i = 3,j = 31; i >=0; i--,j++){
            Unsafe.putByte(mp_code_base + j, data[i]);
        }
        return mp();
    }

}
