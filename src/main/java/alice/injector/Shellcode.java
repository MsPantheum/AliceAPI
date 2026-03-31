package alice.injector;

import alice.util.ClassUtil;
import alice.util.MethodInfo;
import alice.util.Unsafe;
import alice.util.constants.AccessFlags;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import java.io.PrintStream;

import static alice.HSDB.typeDataBase;
import static alice.util.AddressUtil.checkNull;
import static alice.util.AddressUtil.getAddressValue;
import static alice.util.constants.AccessFlags.*;

public class Shellcode {
    private static final long _from_compiled_entry_offset = typeDataBase.lookupType("Method").getAddressField("_from_compiled_entry").getOffset();
    private static final long _from_interpreted_entry_offset = typeDataBase.lookupType("Method").getAddressField("_from_interpreted_entry").getOffset();

    public static long getInterpretedEntry(Class<?> target, String name, String desc) {
        return getInterpretedEntry(ClassUtil.<InstanceKlass>getKlass(target).findMethod(name, desc));
    }

    public static long getInterpretedEntry(Method method) {
        if (method != null) {
            return Unsafe.getLong(getAddressValue(method.getAddress()) + _from_interpreted_entry_offset);
        }
        return 0;
    }

    public static boolean setInterpretedEntry(Method method, long neo) {
        if (method != null) {
            Unsafe.putLong(getAddressValue(method.getAddress()) + _from_interpreted_entry_offset, neo);
            return true;
        }
        return false;
    }

    public static boolean setInterpretedEntry(Class<?> target, String name, String desc, long neo) {
        return setInterpretedEntry(ClassUtil.<InstanceKlass>getKlass(target).findMethod(name, desc), neo);
    }

    public static long getPointer2InterpretedEntry(Method method) {
        return method != null ? getAddressValue(method.getAddress()) + _from_interpreted_entry_offset : 0;
    }

    public static long getPointer2InterpretedEntry(Class<?> target, String name, String desc) {
        return getPointer2InterpretedEntry(ClassUtil.<InstanceKlass>getKlass(target).findMethod(name, desc));
    }

    public static long getCompiledEntry(Method method) {
        if (method != null) {
            return Unsafe.getLong(getAddressValue(method.getAddress()) + _from_compiled_entry_offset);
        }
        return 0;
    }

    public static long getCompiledEntry(Class<?> target, String name, String desc) {
        return getCompiledEntry(ClassUtil.<InstanceKlass>getKlass(target).findMethod(name, desc));
    }

    public static boolean setCompiledEntry(Method method, long neo) {
        if (method != null) {
            Unsafe.putLong(getAddressValue(method.getAddress()) + _from_compiled_entry_offset, neo);
            return true;
        }
        return false;
    }

    public static boolean setCompiledEntry(Class<?> target, String name, String desc, long neo) {
        return setCompiledEntry(ClassUtil.<InstanceKlass>getKlass(target).findMethod(name, desc), neo);
    }

    public static long getPointer2CompiledEntry(Method method) {
        return method != null ? getAddressValue(method.getAddress()) + _from_compiled_entry_offset : 0;
    }

    public static long getPointer2CompiledEntry(Class<?> target, String name, String desc) {
        return getPointer2CompiledEntry(ClassUtil.<InstanceKlass>getKlass(target).findMethod(name, desc));
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

    public static long getCompiledEntry(MethodInfo mi) {
        return getCompiledEntry(mi.holder, mi.methodName, mi.methodDesc);
    }

    public static long getInterpretedEntry(MethodInfo mi) {
        return getInterpretedEntry(mi.holder, mi.methodName, mi.methodDesc);
    }

    public static boolean setCompiledEntry(MethodInfo mi, long neo) {
        return setCompiledEntry(mi.holder, mi.methodName, mi.methodDesc, neo);
    }

    public static boolean setInterpretedEntry(MethodInfo mi, long neo) {
        return setInterpretedEntry(mi.holder, mi.methodName, mi.methodDesc, neo);
    }

    public static long getPointer2CompiledEntry(MethodInfo mi) {
        return getPointer2CompiledEntry(mi.holder, mi.methodName, mi.methodDesc);
    }

    public static long getPointer2InterpretedEntry(MethodInfo mi) {
        return getPointer2InterpretedEntry(mi.holder, mi.methodName, mi.methodDesc);
    }

    private static final long _access_flags_offset = typeDataBase.lookupType("Method").getField("_access_flags").getOffset();

    public static void antiOptimization(Method method) {
        int access = (int) (method.getAccessFlags() & JVM_ACC_WRITTEN_FLAGS);
        access = access | JVM_ACC_NOT_C1_COMPILABLE | JVM_ACC_NOT_C2_COMPILABLE | AccessFlags.JVM_ACC_NOT_C2_OSR_COMPILABLE;

        Unsafe.putInt(getAddressValue(method) + _access_flags_offset, access);
    }
}
