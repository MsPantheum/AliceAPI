package alice.util;

import alice.exception.BadEnvironment;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Metadata;
import sun.jvm.hotspot.oops.Method;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.utilities.MethodArray;

import java.util.List;

import static alice.HSDB.typeDataBase;

public final class AddressUtil {

    public static void checkNull(long address) {
        if (address == 0) {
            throw new NullPointerException();
        }
    }

    public static void checkNull(long... addresses) {
        for (long address : addresses) {
            checkNull(address);
        }
    }

    public static long getObjAddress(Object object) {
        if (object == null) {
            return 0L;
        } else {
            Object[] array = new Object[]{object};
            long baseOffset = Unsafe.arrayBaseOffset(Object[].class);
            int addressSize = Unsafe.ADDRESS_SIZE;
            long location;
            switch (addressSize) {
                case 4:
                    location = Unsafe.getInt(array, baseOffset);
                    break;
                case 8:
                    location = Unsafe.getLong(array, baseOffset);
                    break;
                default:
                    throw new BadEnvironment("unsupported address size: " + addressSize);
            }

            return location * 8L;
        }
    }

    public static long align_page(long address) {
        return address & -Unsafe.PAGE_SIZE;
    }

    public static long align(long address, long bound) {
        //noinspection PointlessBitwiseExpression
        return (address + bound - 1) & ~(bound - 1);
    }

    public static long align(long address) {
        return align(address, Unsafe.ADDRESS_SIZE);
    }

    public static final long klass_offset;
    public static final long oopSize;

    static {
        oopSize = VM.getVM().getOopSize();
        klass_offset = typeDataBase.lookupType("java_lang_Class").getCIntegerField("_klass_offset").getValue();
    }

    public static long getKlassAddress(Class<?> cls) {
        return oopSize == 8 ? Unsafe.getLong(cls, klass_offset) : Unsafe.getInt(cls, klass_offset) & 0xffffffffL;
    }

    public static long getMethodAddress(MethodInfo methodInfo) {
        InstanceKlass klass = ClassUtil.getKlass(methodInfo.holder);
        @SuppressWarnings("unchecked") List<Method> methods = klass.getImmediateMethods();
        for (Method method : methods) {
            if (method.getName().asString().equals(methodInfo.methodName) && method.getSignature().asString().equals(methodInfo.methodDesc)) {
                return Converter.getAddressValue(method.getAddress());
            }
        }
        return 0;
    }

    private static final long method_dataFieldOffset;

    static {
        method_dataFieldOffset = typeDataBase.lookupType("Array<Method*>").getField("_data").getOffset();
    }

    public static long getPointer2Method(MethodInfo methodInfo) {
        InstanceKlass klass = ClassUtil.getKlass(methodInfo.holder);
        MethodArray methods = klass.getMethods();
        long start = Converter.getAddressValue(methods.getAddress());
        long offset = methods.getElemType().getSize();
        for (int i = 0; i < methods.length(); i++) {
            long p = start + method_dataFieldOffset + i * offset;
            long m = Unsafe.getAddress(start + method_dataFieldOffset + i * offset);
            Method method = (Method) Metadata.instantiateWrapperFor(Converter.toAddress(m));
            if (method.getName().asString().equals(methodInfo.methodName) && method.getSignature().asString().equals(methodInfo.methodDesc)) {
                return p;
            }
        }
        return 0;
    }

}
