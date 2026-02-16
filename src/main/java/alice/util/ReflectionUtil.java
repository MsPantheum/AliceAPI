package alice.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public class ReflectionUtil {
    private static final MethodHandles.Lookup IMPL_LOOKUP;

    static {
        IMPL_LOOKUP = Unsafe.allocateInstance(MethodHandles.Lookup.class);
        try {
            Field f = MethodHandles.Lookup.class.getDeclaredField("lookupClass");
            Unsafe.putObject(IMPL_LOOKUP,Unsafe.objectFieldOffset(f),Object.class);
            f = MethodHandles.Lookup.class.getDeclaredField("allowedModes");
            Unsafe.putInt(IMPL_LOOKUP,Unsafe.objectFieldOffset(f),-1);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle findVirtual(Class<?> owner, String name, MethodType methodType) {
        try {
            return IMPL_LOOKUP.findVirtual(owner,name,methodType);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle findStatic(Class<?> owner, String name, MethodType methodType) {
        try {
            return IMPL_LOOKUP.findStatic(owner, name, methodType);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle findConstructor(Class<?> owner, MethodType methodType) {
        try {
            return IMPL_LOOKUP.findConstructor(owner,methodType);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle findSpecial(Class<?> owner,String name,MethodType methodType,Class<?> caller){
        try {
            return IMPL_LOOKUP.findSpecial(owner, name, methodType, caller);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
