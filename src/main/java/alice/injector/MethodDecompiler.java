package alice.injector;

import alice.Platform;
import alice.util.*;
import sun.jvm.hotspot.code.NMethod;
import sun.jvm.hotspot.debugger.Address;
import sun.jvm.hotspot.oops.Klass;
import sun.jvm.hotspot.oops.Method;
import sun.jvm.hotspot.types.Field;
import sun.jvm.hotspot.types.Type;

import java.lang.invoke.*;
import java.util.Iterator;

import static alice.HSDB.typeDataBase;
import static alice.util.Converter.getAddressValue;

public class MethodDecompiler {

    private static final long _marked_for_deoptimization_offset = Platform.JAVA_VERSION <= 8 ? typeDataBase.lookupType("nmethod").getField("_marked_for_deoptimization").getOffset() : -1;
    private static final long _state_offset = typeDataBase.lookupType("nmethod").getField("_state").getOffset();
    private static final long _lock_count_offset = typeDataBase.lookupType("nmethod").getField("_lock_count").getOffset();
    private static final long _comp_level_offset = typeDataBase.lookupType("nmethod").getField("_comp_level").getOffset();

    public static int getCompLevel(NMethod nmethod) {
        return Unsafe.getInt(Converter.getAddressValue(nmethod), _comp_level_offset);
    }

    public static void mark4deoptimization(NMethod nmethod) {
        if (!nmethod.canBeDeoptimized()) {
            throw new IllegalStateException(nmethod.getName().concat(" can't be deoptimized!"));
        }
        if (_marked_for_deoptimization_offset != -1) {
            Unsafe.putByte(Converter.getAddressValue(nmethod) + _marked_for_deoptimization_offset, (byte) 1);
        }
        //Unsafe.putByte(Converter.getAddressValue(nmethod) + _state_offset, (byte) 1);
    }

    public static void lockNMethod(NMethod nmethod) {
        long target_address = getAddressValue(nmethod) + _lock_count_offset;
        int lock = Unsafe.getInt(target_address);
        Unsafe.loadFence();
        Unsafe.storeFence();
        Unsafe.putInt(target_address, lock + 1);
        Unsafe.fullFence();
    }

    public static void unlockNMethod(NMethod nmethod) {
        long target_address = getAddressValue(nmethod) + _lock_count_offset;
        int lock = Unsafe.getInt(target_address);
        Unsafe.loadFence();
        if (lock == 0) {
            throw new IllegalArgumentException();
        }
        Unsafe.storeFence();
        Unsafe.putInt(target_address, lock - 1);
        Unsafe.fullFence();
    }

    private static final Class<?> MHN;
    private static final boolean CallSiteContextAvailable;
    private static final Class<?> CallSiteContext;
    private static final MethodHandle setCallSiteTargetNormal;
    private static final MethodHandle clearCallSiteContext;
    private static final long CallSiteContext_vmdependencies_offset;
    private static final long _dependencies_offset;

    static {
        try {
            MHN = Class.forName("java.lang.invoke.MethodHandleNatives");
            CallSiteContextAvailable = Platform.JAVA_VERSION > 8 && Platform.JAVA_VERSION < 21;
            CallSiteContext = CallSiteContextAvailable ? Class.forName("java.lang.invoke.MethodHandleNatives$CallSiteContext") : null;
            setCallSiteTargetNormal = CallSiteContextAvailable ? ReflectionUtil.findStatic(MHN, "setCallSiteTargetNormal", MethodType.methodType(void.class, CallSite.class, MethodHandle.class)) : null;
            clearCallSiteContext = CallSiteContextAvailable ? ReflectionUtil.findStatic(MHN, "clearCallSiteContext", MethodType.methodType(void.class, CallSiteContext)) : null;
            CallSiteContext_vmdependencies_offset = CallSiteContextAvailable ? Unsafe.objectFieldOffset(ReflectionUtil.getField(CallSiteContext, "vmdependencies")) : -1;
            Type type = typeDataBase.lookupType("InstanceKlass");
            Iterator<Field> fields = type.getFields();
            long tmp = -1;
            while (fields.hasNext()) {
                Field field = fields.next();
                if (!field.isStatic()) {
                    if (field.getName().equals("_dependencies") || field.getName().equals("_dep_context")) {
                        tmp = field.getOffset();
                        break;
                    }
                }
            }
            if (tmp == -1) {
                long klass = Converter.getAddressValue((Klass) ClassUtil.getKlass(String.class));
                long size = typeDataBase.lookupType("InstanceKlass").getSize();
                for (int i = 0; i < size; i++) {
                    long _try = Unsafe.getLong(klass + i);
                    if (MemoryUtil.readable(_try)) {
                        _try = Unsafe.getLong(_try);
                        if (MemoryUtil.readable(_try)) {
                            Address address = Converter.toAddress(_try);
                            Type t = typeDataBase.guessTypeForAddress(address);
                            if (t.getName().equals("nmethod")) {
                                tmp = i;
                                break;
                            }
                        }
                    }
                }
            }

            if (tmp != -1) {
                _dependencies_offset = tmp;
            } else {
                throw new IllegalStateException("Cannot find offset of _dependencies(or _dep_context)");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void tryDecompile(Method method) {
        NMethod nmethod = method.getNativeMethod();
        lockNMethod(nmethod);
        mark4deoptimization(nmethod);
        unlockNMethod(nmethod);
        if (!CallSiteContextAvailable) {
            MutableCallSite mcs = new MutableCallSite(Converter.convert(method));
            try {
                setCallSiteTargetNormal.invoke(mcs, MethodHandles.constant(long.class, 114514191980L));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            Object contextObject = Unsafe.allocateInstance(CallSiteContext);
            long dep = Unsafe.getLong(Converter.getAddressValue(method.getMethodHolder()) + _dependencies_offset);
            Unsafe.putLong(contextObject, CallSiteContext_vmdependencies_offset, dep);
            try {
                clearCallSiteContext.invoke(contextObject);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }
}
