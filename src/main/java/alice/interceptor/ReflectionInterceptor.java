package alice.interceptor;

import alice.Platform;
import alice.log.Logger;
import alice.util.BytecodeUtil;
import alice.util.ReflectionUtil;
import alice.util.Unsafe;
import org.objectweb.asm.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleInfo;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ReflectionInterceptor {

    private static final Class<?> NativeMethodAccessorClass;
    private static final Field sun_reflect_NativeMethodAccessorImpl_method_field;
    private static final long sun_reflect_NativeMethodAccessorImpl_method_offset;
    private static final MethodHandle invoke0;

    static {
        try {
            NativeMethodAccessorClass = Class.forName(Platform.jigsaw ? Platform.JAVA_VERSION >= 26 ? "jdk.internal.reflect.DirectMethodHandleAccessor$NativeAccessor" : "jdk.internal.reflect.NativeMethodAccessorImpl" : "sun.reflect.NativeMethodAccessorImpl");
            sun_reflect_NativeMethodAccessorImpl_method_field = ReflectionUtil.getField(NativeMethodAccessorClass, "method");
            sun_reflect_NativeMethodAccessorImpl_method_offset = Unsafe.objectFieldOffset(sun_reflect_NativeMethodAccessorImpl_method_field);
            invoke0 = ReflectionUtil.findStatic(NativeMethodAccessorClass, "invoke0", MethodType.methodType(Object.class, Method.class, Object.class, Object[].class));
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
        Logger.MAIN.warn("Checking invoke of:".concat(method.getDeclaringClass().getName().concat(".").concat(method.toString())));
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
        } else if (method.getDeclaringClass() == NativeMethodAccessorClass) {
            if (method.getName().equals("invoke")) {
                //The offset is no longer persistent.
                method = Platform.JAVA_VERSION < 12 ? Unsafe.getObject(obj, sun_reflect_NativeMethodAccessorImpl_method_offset) : Unsafe.getObject(obj, Unsafe.objectFieldOffset(sun_reflect_NativeMethodAccessorImpl_method_field));
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
        method.setAccessible(true);
        return method.invoke(obj, args);
    }

    public static void set(Field field, Object obj, Object value) throws IllegalAccessException {
        if (field.getDeclaringClass() == URLClassLoader.class && field.getName().equals("ucp")) {
            if (UnsafeInterceptor.LOG_UCP_REPLACE) {
                Logger.MAIN.warn("Warning: ucp replace detected!");
                return;
            }
        }
        field.set(obj, value);
    }

    public static boolean checkMethodHandle(MethodHandle mh) {

        return false;
    }

    public static class MethodHandleInterceptorProvider implements Opcodes {

        private static final Set<String> records = new HashSet<>();

        private static class TargetInfo {
            private final String method_name;
            private final String descriptor;

            private TargetInfo(String method_name, String descriptor) {
                this.method_name = method_name;
                this.descriptor = descriptor;
            }
        }

        public static boolean isInterceptor(String name) {
            return records.contains(name);
        }

        private static final Map<String, TargetInfo> targets = new HashMap<>();

        public static void addTarget(String class_name, String method_name, String descriptor) {
            records.add(class_name);
            targets.put(class_name, new TargetInfo(method_name, descriptor));
        }

        public static byte[] provide(String name) {
            TargetInfo info = targets.remove(name);
            if (info != null) {
                name = name.substring(0, name.length() - 6);
                ClassWriter cw = new ClassWriter(0);
                cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, name, null, "java/lang/Object", null);
                MethodVisitor mv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, info.method_name, info.descriptor, null, null);
                Type t = Type.getMethodType(info.descriptor);
                Type ret_type = t.getReturnType();
                Type[] raw_args = t.getArgumentTypes();
                Type[] args = new Type[raw_args.length - 1];
                if (args.length != 0) {
                    System.arraycopy(raw_args, 1, args, 0, raw_args.length - 1);
                }
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(INVOKESTATIC, "alice/interceptor/ReflectionInterceptor", "checkMethodHandle", "(Ljava/lang/invoke/MethodHandle;)Z", false);
                Label _if = new Label();
                mv.visitJumpInsn(IFEQ, _if);
                BytecodeUtil.generateValue(mv, ret_type);
                mv.visitInsn(ret_type.getOpcode(IRETURN));
                mv.visitLabel(_if);
                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                int index = 1;
                mv.visitVarInsn(ALOAD, 0);
                int max = 1;
                for (Type arg : args) {
                    mv.visitVarInsn(arg.getOpcode(ILOAD), index);
                    index += arg.getSize();
                    max += arg.getSize();
                }

                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/invoke/MethodHandle", info.method_name, "(".concat(info.descriptor.substring(32)), false);
                mv.visitInsn(ret_type.getOpcode(IRETURN));
                mv.visitMaxs(max, max);
                return cw.toByteArray();
            }
            return null;
        }
    }
}
