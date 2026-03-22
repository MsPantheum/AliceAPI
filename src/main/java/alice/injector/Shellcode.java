package alice.injector;

import alice.util.MethodInfo;
import alice.util.Unsafe;
import sun.jvm.hotspot.code.NMethod;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Metadata;
import sun.jvm.hotspot.oops.Method;

import java.io.PrintStream;

import static alice.HSDB.typeDataBase;
import static alice.util.AddressUtil.*;

public class Shellcode {
    private static final long _from_compiled_entry_offset = typeDataBase.lookupType("Method").getAddressField("_from_compiled_entry").getOffset();
    private static final long _verified_entry_point_offset = typeDataBase.lookupType("nmethod").getAddressField("_verified_entry_point").getOffset();

//    public static void setCompiledEntry(Class<?> target,String name,String desc,long neo){
//
//    }

    public static long getCompiledEntry(Class<?> target, String name, String desc){
        InstanceKlass klass = (InstanceKlass) Metadata.instantiateWrapperFor(toAddress(getKlassAddress(target)));
        Method method = klass.findMethod(name, desc);
        if (method != null) {
            NMethod nmethod = method.getNativeMethod();
            if (nmethod != null) {
                return getAddressValue(nmethod.getVerifiedEntryPoint());
            }
        }
        return 0;
    }

    public static boolean setCompiledEntry(Class<?> target, String name, String desc,long neo){
        InstanceKlass klass = (InstanceKlass) Metadata.instantiateWrapperFor(toAddress(getKlassAddress(target)));
        Method method = klass.findMethod(name, desc);
        if (method != null) {
            Unsafe.putLong(getAddressValue(method.getAddress()) + _from_compiled_entry_offset, neo);
            NMethod nmethod = method.getNativeMethod();
            Unsafe.putLong(getAddressValue(nmethod.getAddress()) + _verified_entry_point_offset, neo);
            return true;
        }
        return false;
    }

    public static long getPointer2CompiledEntry(Class<?> target, String name, String desc){
        InstanceKlass klass = (InstanceKlass) Metadata.instantiateWrapperFor(toAddress(getKlassAddress(target)));
        Method method = klass.findMethod(name, desc);
        return method != null ? getAddressValue(method.getAddress()) + _from_compiled_entry_offset : 0;
    }

    public static long inject(byte[] payload, Class<?> target, String name, String desc) {
        long address = getCompiledEntry(target, name, desc);
        checkNull(address);
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
        return address;
    }

    public static void dump(long start, long size, PrintStream out){
        checkNull(start);
        out.println("----------START_DUMP----------");
        for(long i = 0; i < size; i++){
            out.printf("0x%x ",Unsafe.getByte(start+i));
        }
        out.println();
        out.println("-----------END_DUMP-----------");
    }

    public static long getCompiledEntry(MethodInfo ori) {
        return getCompiledEntry(ori.holder,ori.methodName,ori.methodDesc);
    }
}
