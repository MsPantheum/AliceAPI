package alice._native.jni;

import alice.injector.Shellcode;
import alice.injector.SymbolLookup;
import alice.util.AddressUtil;
import alice.util.ClassUtil;
import alice.util.MemoryUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

//JNI_GetCreatedJavaVMs(JavaVM **, jsize, jsize *);

public final class JNI_GetCreatedJavaVMs {

    @SuppressWarnings({"DuplicatedCode"})
    private static int holder() {
        return (int) System.nanoTime();
    }

    private static final long code_base;

    static {
        holder();
        byte[] payload = new byte[37];
        payload[0] = (byte) 0x48;
        payload[1] = (byte) 0xbf;
        //vmBuf here
        payload[10] = (byte) 0x48;
        payload[11] = (byte) 0xba;
        //nVMs here
        payload[20] = (byte) 0x48;
        payload[21] = (byte) 0xb8;
        //function
        payload[30] = (byte) 0xbe;
        //bufLen here
        payload[35] = (byte) 0xff;
        payload[36] = (byte) 0xe0;
        code_base = MemoryUtil.allocate(payload.length);
        AddressUtil.checkNull(code_base);
        Unsafe.writeBytes(code_base, payload);
        long function = SymbolLookup.lookup("JNI_GetCreatedJavaVMs");
        AddressUtil.checkNull(function);
        Unsafe.putLong(code_base + 22, function);
        InstanceKlass klass = ClassUtil.getKlass(JNI_GetCreatedJavaVMs.class);
        Method method = klass.findMethod("holder", "()I");
        Shellcode.antiOptimization(method);
        Shellcode.setInterpretedEntry(method, code_base);
    }

    public synchronized static int invoke(long vmBuf, int bufLen, long nVMs) {
        Unsafe.putLong(code_base + 2, vmBuf);
        Unsafe.putInt(code_base + 31, bufLen);
        Unsafe.putLong(code_base + 12, nVMs);
        return holder();
    }
}
