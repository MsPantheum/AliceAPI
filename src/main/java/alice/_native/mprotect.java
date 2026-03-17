package alice._native;

//int mprotect (void *__addr, size_t __len, int __prot)

import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.Unsafe;

public class mprotect {
    @SuppressWarnings({"DuplicatedCode", "ReassignedVariable", "ConstantValue", "lossy-conversions", "UnusedAssignment"})
    private static int holder() {
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

    private static final long code_base;

    static {
        for(int i = 0; i < 20000 ; i++){
            //noinspection ResultOfMethodCallIgnored
            holder();
        }
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
        payload[30] = (byte) 0xba;
        //__prot here
        payload[35] = (byte) 0xff;
        payload[36] = (byte) 0xe0;
        long tmp = Shellcode.inject(payload, mprotect.class,"holder","()I");
        assert tmp != 0;
        code_base = tmp;
        Unsafe.putLong(code_base + 22,func);
    }

    public synchronized static int invoke(long __addr,long __len,int __prot){
        Unsafe.putLong(code_base + 2,__addr);
        Unsafe.putLong(code_base + 12,__len);
        Unsafe.putInt(code_base + 31,__prot);
        return holder();
    }

}
