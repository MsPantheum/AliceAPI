package alice.util;

import alice.Platform;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
 * Util for dynamic method invoking.
 */
public class ReflectionUtil {

    /* A trusted lookup. */
    private static final MethodHandles.Lookup IMPL_LOOKUP;

    /*
     * Get a trusted lookup.
     * @return a trusted lookup.
     */
    public static MethodHandles.Lookup lookup() {
        return IMPL_LOOKUP;
    }

    static {
        IMPL_LOOKUP = Unsafe.allocateInstance(MethodHandles.Lookup.class);
        try {
            if (!Platform.jigsaw) {
                Field f = MethodHandles.Lookup.class.getDeclaredField("lookupClass");
                Unsafe.putObject(IMPL_LOOKUP, Unsafe.objectFieldOffset(f), Object.class);
                f = MethodHandles.Lookup.class.getDeclaredField("allowedModes");
                Unsafe.putInt(IMPL_LOOKUP, Unsafe.objectFieldOffset(f), -1);
            } else {
                Module module = Class.class.getModule();
                long m_offset = Unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));
                Unsafe.putObject(Class.class, m_offset, ReflectionUtil.class.getModule());
                Method m = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
                m.setAccessible(true);
                Field[] fields = (Field[]) m.invoke(MethodHandles.Lookup.class, false);
                boolean success1 = false, success2 = false;
                for (Field field : fields) {
                    if (field.getName().equals("lookupClass")) {
                        Unsafe.putObject(IMPL_LOOKUP, Unsafe.objectFieldOffset(field), Object.class);
                        success1 = true;
                    } else if (field.getName().equals("allowedModes")) {
                        Unsafe.putInt(IMPL_LOOKUP, Unsafe.objectFieldOffset(field), -1);
                        success2 = true;
                    }
                }
                if (!success1 || !success2) {
                    throw new IllegalStateException();
                }
                Unsafe.putObject(Class.class, m_offset, module);
            }
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandleInfo resolve(MethodHandle mh) {
        return IMPL_LOOKUP.revealDirect(mh);
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


    private static final MethodHandle getFields;
    private static final MethodHandle getMethods;
    private static final MethodHandle getGenericSignature;

    static {
        try {
            getFields = IMPL_LOOKUP.findVirtual(Class.class, "getDeclaredFields0", MethodType.methodType(Field[].class, boolean.class));
            getMethods = IMPL_LOOKUP.findVirtual(Class.class, "getDeclaredMethods0", MethodType.methodType(Method[].class, boolean.class));
            getGenericSignature = IMPL_LOOKUP.findVirtual(Method.class, "getGenericSignature", MethodType.methodType(String.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getGenericSignature(Method method) {
        try {
            return (String) getGenericSignature.invoke(method);
        } catch (Throwable e) {
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

    public static Method[] getMethods(Class<?> clazz) {
        try {
            return (Method[]) getMethods.invoke(clazz, false);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        for (Method m : getMethods(clazz)) {
            if (name.equals(m.getName()) && parameterTypes == m.getParameterTypes()) {
                return m;
            }
        }
        throw new NoSuchMethodError();
    }

    public static VarHandle findVarHandle(Class<?> recv, String name, Class<?> type) {
        try {
            return IMPL_LOOKUP.findVarHandle(recv, name, type);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static VarHandle findStaticVarHandle(Class<?> decl, String name, Class<?> type) {
        try {
            return IMPL_LOOKUP.findStaticVarHandle(decl, name, type);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
