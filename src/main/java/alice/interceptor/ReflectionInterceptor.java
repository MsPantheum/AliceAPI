package alice.interceptor;

import alice.util.DebugUtil;
import alice.util.ReflectionUtil;
import alice.util.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleInfo;
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

    private static class InvokeResult {
        private final Object ret;
        private final boolean success;

        private InvokeResult(Object ret, boolean success) {
            this.ret = ret;
            this.success = success;
        }
    }

    private static InvokeResult checkMethodHandle(MethodHandle mh, Object... args) throws Throwable {
        MethodHandleInfo info = ReflectionUtil.resolve(mh);
        if (info.getDeclaringClass() == MethodHandle.class) {
            if (info.getName().equals("invoke")) {
                assert args.length == 2;
                mh = (MethodHandle) args[0];
                args = (Object[]) args[1];
                return new InvokeResult(invoke(mh, args), true);
            } else if (info.getName().equals("invokeExact")) {
                assert args.length == 2;
                mh = (MethodHandle) args[0];
                args = (Object[]) args[1];
                return new InvokeResult(invokeExact(mh, args), true);
            }
        } else if (info.getDeclaringClass() == Method.class) {
            if (info.getName().equals("invoke")) {
                assert args.length == 3;
                Method method = (Method) args[0];
                Object obj = args[1];
                args = (Object[]) args[2];
                return new InvokeResult(invoke(method, obj, args), true);
            }
        } else if (info.getDeclaringClass() == Field.class) {
            assert args.length == 3;
            Field field = (Field) args[0];
            Object obj = args[1];
            Object value = args[2];
            set(field, obj, value);
            return new InvokeResult(null, true);
        }

        return new InvokeResult(null, false);
    }

    public static Object invoke(MethodHandle mh, Object... args) throws Throwable {
        InvokeResult ir = checkMethodHandle(mh, args);
        if (ir.success) {
            return ir.ret;
        }
        return mh.invoke(args);
    }

    public static Object invokeExact(MethodHandle mh, Object... args) throws Throwable {
        InvokeResult ir = checkMethodHandle(mh, args);
        if (ir.success) {
            return ir.ret;
        }
        return mh.invokeExact(args);
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
            if (method.getName().equals("invoke")) {
                MethodHandle mh = (MethodHandle) obj;
                return invoke(mh, args);
            } else if (method.getName().equals("invokeExact")) {
                MethodHandle mh = (MethodHandle) obj;
                return invokeExact(mh, args);
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
