package alice.util;

import org.objectweb.asm.Type;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {
    private static final MethodHandles.Lookup IMPL_LOOKUP;

    static {
        IMPL_LOOKUP = Unsafe.allocateInstance(MethodHandles.Lookup.class);
        try {
            Field f = MethodHandles.Lookup.class.getDeclaredField("lookupClass");
            Unsafe.putObject(IMPL_LOOKUP, Unsafe.objectFieldOffset(f), Object.class);
            f = MethodHandles.Lookup.class.getDeclaredField("allowedModes");
            Unsafe.putInt(IMPL_LOOKUP, Unsafe.objectFieldOffset(f), -1);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle findVirtual(Class<?> owner, String name, MethodType methodType) {
        try {
            return IMPL_LOOKUP.findVirtual(owner, name, methodType);
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
            return IMPL_LOOKUP.findConstructor(owner, methodType);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle findSpecial(Class<?> owner, String name, MethodType methodType, Class<?> special_caller) {
        try {
            return IMPL_LOOKUP.findSpecial(owner, name, methodType, special_caller);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle get(Method method) {
        try {
            return IMPL_LOOKUP.unreflect(method);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle get(sun.jvm.hotspot.oops.Method method) {
        try {
            Class<?> holder = MemoryUtil.getObject(method.getMethodHolder().getJavaMirror().getHandle());
            String name = method.getName().asString();
            Type type = Type.getMethodType(method.getSignature().asString());
            Class<?> ret = Class.forName(type.getReturnType().getClassName());

            Type[] arg_types = type.getArgumentTypes();
            Class<?>[] args = new Class<?>[arg_types.length];
            for (int i = 0; i < args.length; i++) {
                args[i] = Class.forName(arg_types[i].getClassName());
            }
            if (method.getAccessFlagsObj().isStatic()) {
                return findStatic(holder, name, MethodType.methodType(ret, args));
            } else {
                return findVirtual(holder, name, MethodType.methodType(ret, args));
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }


    private static final MethodHandle getFields;

    static {
        try {
            getFields = IMPL_LOOKUP.findVirtual(Class.class, "getDeclaredFields0", MethodType.methodType(Field[].class, boolean.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Field[] getFields(Class<?> clazz) {
        try {
            return (Field[]) getFields.invoke(clazz, false);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getField(Class<?> clazz, String name) {
        for (Field f : getFields(clazz)) {
            if (name.equals(f.getName())) {
                return f;
            }
        }
        throw new NoSuchFieldError();
    }

}
