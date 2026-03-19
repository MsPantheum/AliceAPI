package alice.injector;

import alice.util.AddressUtil;
import alice.util.MethodInfo;
import alice.util.Unsafe;
import sun.jvm.hotspot.types.Type;

import java.io.PrintStream;

import static alice.HSDB.typeDataBase;

public class Shellcode {

    public static long getCompiledEntry(Class<?> target, String name, String desc){
        int oopSize = typeDataBase.lookupIntConstant("oopSize");
        long klass = AddressUtil.getKlassAddress(target);

        long methodArray = Unsafe.getAddress(klass + typeDataBase.lookupType("InstanceKlass").getField("_methods").getOffset());
        int methodCount = Unsafe.getInt(methodArray);
        long methods = methodArray + typeDataBase.lookupType("Array<Method*>").getAddressField("_data").getOffset();

        long constMethodOffset = typeDataBase.lookupType("Method").getAddressField("_constMethod").getOffset();
        Type constMethodType = typeDataBase.lookupType("ConstMethod");
        Type constantPoolType = typeDataBase.lookupType("ConstantPool");
        long constantPoolOffset = constMethodType.getAddressField("_constants").getOffset();
        long nameIndexOffset = constMethodType.getAddressField("_name_index").getOffset();
        long signatureIndexOffset = constMethodType.getCIntegerField("_signature_index").getOffset();
        long _from_compiled_entry = typeDataBase.lookupType("Method").getAddressField("_from_compiled_entry").getOffset();

        for (int i = 0; i < methodCount; i++) {
            long method = Unsafe.getAddress(methods + (long) i * oopSize);
            long constMethod = Unsafe.getAddress(method + constMethodOffset);

            long constantPool = Unsafe.getAddress(constMethod + constantPoolOffset);
            int nameIndex = Unsafe.getShort(constMethod + nameIndexOffset) & 0xffff;
            int signatureIndex = Unsafe.getShort(constMethod + signatureIndexOffset) & 0xffff;

            String _name = AddressUtil.readSymbol(constantPool + constantPoolType.getSize() + (long) nameIndex * oopSize);
            String _desc = desc != null ? AddressUtil.readSymbol(
                    constantPool + constantPoolType.getSize() + (long) signatureIndex * oopSize) : null;
            if (name.equals(_name)
                    && (desc == null || desc.equals(_desc))) {

                return Unsafe.getAddress(method + _from_compiled_entry);

            }
        }
        return 0;
    }

    public static boolean setCompiledEntry(Class<?> target, String name, String desc,long neo){
        int oopSize = typeDataBase.lookupIntConstant("oopSize");
        long klass = AddressUtil.getKlassAddress(target);

        long methodArray = Unsafe.getAddress(klass + typeDataBase.lookupType("InstanceKlass").getField("_methods").getOffset());
        int methodCount = Unsafe.getInt(methodArray);
        long methods = methodArray + typeDataBase.lookupType("Array<Method*>").getAddressField("_data").getOffset();

        long constMethodOffset = typeDataBase.lookupType("Method").getAddressField("_constMethod").getOffset();
        Type constMethodType = typeDataBase.lookupType("ConstMethod");
        Type constantPoolType = typeDataBase.lookupType("ConstantPool");
        long constantPoolOffset = constMethodType.getAddressField("_constants").getOffset();
        long nameIndexOffset = constMethodType.getAddressField("_name_index").getOffset();
        long signatureIndexOffset = constMethodType.getCIntegerField("_signature_index").getOffset();
        long _from_compiled_entry = typeDataBase.lookupType("Method").getAddressField("_from_compiled_entry").getOffset();

        for (int i = 0; i < methodCount; i++) {
            long method = Unsafe.getAddress(methods + (long) i * oopSize);
            long constMethod = Unsafe.getAddress(method + constMethodOffset);

            long constantPool = Unsafe.getAddress(constMethod + constantPoolOffset);
            int nameIndex = Unsafe.getShort(constMethod + nameIndexOffset) & 0xffff;
            int signatureIndex = Unsafe.getShort(constMethod + signatureIndexOffset) & 0xffff;

            String _name = AddressUtil.readSymbol(constantPool + constantPoolType.getSize() + (long) nameIndex * oopSize);
            String _desc = desc != null ? AddressUtil.readSymbol(
                    constantPool + constantPoolType.getSize() + (long) signatureIndex * oopSize) : null;
            if (name.equals(_name)
                    && (desc == null || desc.equals(_desc))) {

                Unsafe.putAddress(method + _from_compiled_entry,neo);
                return true;

            }
        }
        return false;
    }

    public static long getPointer2CompiledEntry(Class<?> target, String name, String desc){
        int oopSize = typeDataBase.lookupIntConstant("oopSize");
        long klass = AddressUtil.getKlassAddress(target);

        long methodArray = Unsafe.getAddress(klass + typeDataBase.lookupType("InstanceKlass").getField("_methods").getOffset());
        int methodCount = Unsafe.getInt(methodArray);
        long methods = methodArray + typeDataBase.lookupType("Array<Method*>").getAddressField("_data").getOffset();

        long constMethodOffset = typeDataBase.lookupType("Method").getAddressField("_constMethod").getOffset();
        Type constMethodType = typeDataBase.lookupType("ConstMethod");
        Type constantPoolType = typeDataBase.lookupType("ConstantPool");
        long constantPoolOffset = constMethodType.getAddressField("_constants").getOffset();
        long nameIndexOffset = constMethodType.getAddressField("_name_index").getOffset();
        long signatureIndexOffset = constMethodType.getCIntegerField("_signature_index").getOffset();
        long _from_compiled_entry = typeDataBase.lookupType("Method").getAddressField("_from_compiled_entry").getOffset();

        for (int i = 0; i < methodCount; i++) {
            long method = Unsafe.getAddress(methods + (long) i * oopSize);
            long constMethod = Unsafe.getAddress(method + constMethodOffset);

            long constantPool = Unsafe.getAddress(constMethod + constantPoolOffset);
            int nameIndex = Unsafe.getShort(constMethod + nameIndexOffset) & 0xffff;
            int signatureIndex = Unsafe.getShort(constMethod + signatureIndexOffset) & 0xffff;

            String _name = AddressUtil.readSymbol(constantPool + constantPoolType.getSize() + (long) nameIndex * oopSize);
            String _desc = desc != null ? AddressUtil.readSymbol(
                    constantPool + constantPoolType.getSize() + (long) signatureIndex * oopSize) : null;
            if (name.equals(_name)
                    && (desc == null || desc.equals(_desc))) {

                return method + _from_compiled_entry;

            }
        }
        return 0;
    }

    public static long inject(byte[] payload, Class<?> target, String name, String desc) {
        long address = getCompiledEntry(target, name, desc);
        if(address == 0){
            return 0;
        }
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
