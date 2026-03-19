package alice._native;

//LPVOID WINAPI VirtualAlloc (LPVOID lpAddress, SIZE_T dwSize, DWORD flAllocationType, DWORD flProtect);

import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.AddressUtil;
import alice.util.Unsafe;

public class VirtualAlloc {

    @SuppressWarnings({"DuplicatedCode", "ReassignedVariable", "ConstantValue", "lossy-conversions", "UnusedAssignment"})
    private static long holder(){
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

        byte[] payload = new byte[45];
        payload[0] = (byte) 0x48;
        payload[1] = (byte) 0xb9;
        //lpAddress here
        payload[10] = (byte) 0x48;
        payload[11] = (byte) 0xba;
        //dwSize here
        payload[20] = (byte) 0x48;
        payload[21] = (byte) 0xb8;
        //function
        payload[30] = (byte) 0x41;
        payload[31] = (byte) 0xb8;
        //flAllocationType here
        payload[36] = (byte) 0x41;
        payload[37] = (byte) 0xb9;
        //flProtect here
        payload[42] = (byte) 0x48;
        payload[43] = (byte) 0xff;
        payload[44] = (byte) 0xe0;

        code_base = Unsafe.allocateMemory(45);//Shellcode.inject(payload,VirtualAlloc.class,"holder","()J");
        Unsafe.writeBytes(code_base, payload);
        Unsafe.putLong(code_base + 22, SymbolLookup.lookup("VirtualAlloc"));
        VirtualProtect.invoke(code_base,1,0x40,0);
        long address = Shellcode.getCompiledEntry(VirtualAlloc.class,"holder","()J");
        boolean success = InlineHook.simpleHook(address,code_base);
        assert success;
    }

    public static synchronized long invoke(long lpAddress,long dwSize,int flAllocationType,int flProtect){
        Unsafe.putLong(code_base + 2,lpAddress);
        Unsafe.putLong(code_base + 12,dwSize);
        Unsafe.putInt(code_base + 32,flAllocationType);
        Unsafe.putInt(code_base + 38,flProtect);
        AddressUtil.println(Shellcode.getCompiledEntry(VirtualAlloc.class,"holder","()J"));
        return holder();
    }
}
