package alice._native.win32;

import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.Unsafe;

//WINBASEAPI WINBOOL WINAPI VirtualProtect (LPVOID lpAddress, SIZE_T dwSize, DWORD flNewProtect, PDWORD lpflOldProtect);

public final class VirtualProtect {

    private static class Bootstrap {

    }

    @SuppressWarnings({"DuplicatedCode", "ReassignedVariable", "ConstantValue", "lossy-conversions", "UnusedAssignment"})
    private static int holder(){
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
        return j % i;
    }

    private static final long code_base;

    static {

        for(int i = 0; i < 20000; i++){
            //noinspection ResultOfMethodCallIgnored
            holder();
        }

        byte[] payload = new byte[62];
        payload[0] = (byte) 0x48;
        payload[1] = (byte) 0x83;
        payload[2] = (byte) 0xec;
        payload[3] = (byte) 0x28;

        payload[4] = (byte) 0x48;
        payload[5] = (byte) 0xb9;
        //lpAddress here

        payload[14] = (byte) 0x48;
        payload[15] = (byte) 0xba;
        //dwSize here

        payload[24] = (byte) 0x49;
        payload[25] = (byte) 0xb9;
        //lpflOldProtect here

        payload[34] = (byte) 0x48;
        payload[35] = (byte) 0xb8;
        //function here

        payload[44] = (byte) 0x41;
        payload[45] = (byte) 0xb8;
        //flNewProtect here
        payload[50] = (byte) 0xff;
        payload[51] = (byte) 0xd0;
        payload[52] = (byte) 0xb8;
        payload[53] = (byte) 0x01;
        payload[54] = (byte) 0x00;
        payload[55] = (byte) 0x00;
        payload[56] = (byte) 0x00;
        payload[57] = (byte) 0x48;
        payload[58] = (byte) 0x83;
        payload[59] = (byte) 0xc4;
        payload[60] = (byte) 0x28;
        payload[61] = (byte) 0xc3;

        code_base = Shellcode.inject(payload, VirtualProtect.class,"holder","()I");
        Unsafe.putLong(code_base + 36, SymbolLookup.lookup("VirtualProtect"));
    }

    public synchronized static int invoke(long lpAddress, int dwSize, int flNewProtect,long lpflOldProtect){
        Unsafe.putLong(code_base + 6,lpAddress);
        Unsafe.putInt(code_base + 16,dwSize);
        Unsafe.putInt(code_base + 46,flNewProtect);
        boolean allocated = false;
        if(lpflOldProtect == 0){
            allocated = true;
            lpflOldProtect = Unsafe.allocateMemory(Integer.BYTES);
        }
        Unsafe.putLong(code_base + 26,lpflOldProtect);
        if(allocated){
            Unsafe.freeMemory(lpflOldProtect);
        }
        return holder();
    }
}
