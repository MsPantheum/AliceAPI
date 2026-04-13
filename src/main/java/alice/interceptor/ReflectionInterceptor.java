package alice.interceptor;

import alice.util.DebugUtil;
import alice.util.ReflectionUtil;
import alice.util.Unsafe;
import org.objectweb.asm.Type;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleInfo;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

public class ReflectionInterceptor {

    private static final long sun_reflect_NativeMethodAccessorImpl_method_offset;

    static {
        try {
            Field field = ReflectionUtil.getField(Class.forName("sun.reflect.NativeMethodAccessorImpl"), "method");
            sun_reflect_NativeMethodAccessorImpl_method_offset = Unsafe.objectFieldOffset(field);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private static Object generateReturnValue(MethodHandle handle) {
        MethodHandleInfo info = ReflectionUtil.resolve(handle);
        Class<?> ret_type_class = info.getMethodType().returnType();
        Type type = Type.getType(ret_type_class);
        int sort = type.getSort();
        switch (sort) {
            case Type.BOOLEAN:
            case Type.CHAR:
            case Type.BYTE:
            case Type.SHORT:
            case Type.INT: {
                return 0;
            }
            case Type.FLOAT: {
                return 0F;
            }
            case Type.LONG: {
                return 0L;
            }
            case Type.DOUBLE: {
                return 0D;
            }
            case Type.ARRAY: {
                if (type.getDimensions() == 1) {
                    return Array.newInstance(ret_type_class.getComponentType(), 0);
                } else {
                    return Array.newInstance(ret_type_class.getComponentType(), new int[]{0});
                }
            }
            case Type.OBJECT: {
                return null;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }

    public static Object invoke(Method method, Object obj, Object... args) throws Throwable {
        if (method.getDeclaringClass() == Method.class && method.getName().equals("invoke")) {
            method = (Method) obj;
            obj = args[0];
            Object[] backup = args;
            args = new Object[args.length - 1];
            System.arraycopy(backup, 1, args, 0, backup.length - 1);
            invoke(method, obj, args);
        } else if (method.getDeclaringClass() == Field.class && method.getName().equals("set")) {
            assert args.length == 2;
            set((Field) obj, args[0], args[1]);
            return null;
        } else if (method.getDeclaringClass().getName().equals("sun.reflect.NativeMethodAccessorImpl")) {
            if (method.getName().equals("invoke")) {
                method = Unsafe.getObject(obj, sun_reflect_NativeMethodAccessorImpl_method_offset);
                assert args.length == 2;
                obj = args[0];
                args = (Object[]) args[1];
                invoke(method, obj, args);
            } else if (method.getName().equals("invoke0")) {
                assert args.length == 3;
                method = (Method) args[0];
                obj = args[1];
                args = (Object[]) args[2];
                invoke(method, obj, args);
            }
        } else if (method.getDeclaringClass() == MethodHandle.class) {
            if (method.getName().startsWith("invoke")) {
                MethodHandle mh = (MethodHandle) obj;
                return generateReturnValue(mh);
            }
        }
        return method.invoke(obj, args);
    }

    public static void set(Field field, Object obj, Object value) throws IllegalAccessException {
        if (field.getDeclaringClass() == URLClassLoader.class && field.getName().equals("ucp")) {
            if (DebugUtil.LOG_UCP_REPLACE) {
                System.out.println("Warning: ucp replace detected!");
                return;
            }
        }
        field.set(obj, value);
    }
}
