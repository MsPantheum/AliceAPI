package alice._native;

import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.AddressUtil;
import alice.util.Unsafe;

import static alice.util.Constants.*;

//void *mmap (void *__addr, size_t __len, int __prot,int __flags, int __fd, __off_t __offset)

public class mmap {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static long holder(){
        System.getenv().keySet();
        return System.nanoTime();
    }

    private static final long code_base;

    static {
        for(int i = 0; i < 20000;i++){
            holder();
        }

        long function = SymbolLookup.lookup("mmap");
        assert function != 0;
        byte[] payload = new byte[58];
        payload[0] = (byte) 0x48;
        payload[1] = (byte) 0xbf;
        //__addr here
        payload[10] = (byte) 0x48;
        payload[11] = (byte) 0xbe;
        //__len here
        payload[20] =  (byte) 0x49;
        payload[21] =  (byte) 0xb9;
        //__offset here
        payload[30] = (byte) 0x48;
        payload[31] = (byte) 0xb8;

        payload[40] = (byte) 0xba;
        //__prot here
        payload[45] = (byte) 0xb9;
        //__flags here
        payload[50] = (byte) 0x41;
        payload[51] = (byte) 0xb8;
        //__fd here
        payload[56]  = (byte) 0xff;
        payload[57]  = (byte) 0xe0;
        code_base = Unsafe.allocateMemory(58);
        Unsafe.writeBytes(code_base,payload);
        Unsafe.putLong(code_base + 32,function);
        mprotect.invoke(AddressUtil.align(code_base),1,PROT_READ | PROT_WRITE | PROT_EXEC);
        long address = Shellcode.getCompiledEntry(mmap.class,"holder","()J");
        InlineHook.simpleHook(address,code_base);
    }

    public synchronized static long invoke(long __addr,long __len,int __prot,int __flags,int __fd,long __offset){
        Unsafe.putLong(code_base + 2,__addr);
        Unsafe.putLong(code_base + 12,__len);
        Unsafe.putLong(code_base + 22,__offset);
        Unsafe.putInt(code_base + 41,__prot);
        Unsafe.putInt(code_base + 46,__flags);
        Unsafe.putInt(code_base + 52,__fd);
        return holder();
    }

}
