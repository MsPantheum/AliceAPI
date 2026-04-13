package alice.interceptor;

import org.objectweb.asm.Opcodes;

//import alice.util.BytecodeUtil;
//import alice.util.ReflectionUtil;
//import org.objectweb.asm.ClassWriter;
//import org.objectweb.asm.MethodVisitor;
//import org.objectweb.asm.Type;
//import java.lang.invoke.MethodHandle;
//import java.lang.invoke.MethodHandleInfo;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;

public class MethodHandleInterceptor implements Opcodes {

//    private static Type getBestArrayType(Type[] args) {
//        Type last = null;
//        for (Type arg : args) {
//            int sort = arg.getSort();
//            if (sort == Type.ARRAY || sort == Type.OBJECT) {
//                return BytecodeUtil.OBJECT_TYPE;
//            } else if (last != null && arg != last) {
//                return BytecodeUtil.OBJECT_TYPE;
//            } else if (last == null) {
//                last = arg;
//            }
//        }
//        return last;
//    }
//
//    public static void generateInterceptor(String clazz, String name, String desc) {
//        String c_name = clazz + "Interceptor" + '_' + Integer.toHexString(desc.hashCode());
//        ClassWriter cw = new ClassWriter(0);
//        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, c_name, null, "java/lang/Object", null);
//        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, name, "(Ljava/lang/invoke/MethodHandle;" + desc.substring(1), null, null);
//        Type type = Type.getType(desc);
//        Type ret_type = type.getReturnType();
//        Type[] args = type.getArgumentTypes();
//
//        mv.visitVarInsn(ALOAD,0);
//        mv.visitMethodInsn(INVOKESTATIC,"alice/util/ReflectionUtil","resolve","(Ljava/lang/invoke/MethodHandle;)Ljava/lang/invoke/MethodHandleInfo;",false);
//        //Now we get the handle info on top of stack.
//        mv.visitInsn(DUP);//DUP!
//
//
//        int locals = 1;
//
//        Type array_type = BytecodeUtil.OBJECT_TYPE;
//        BytecodeUtil.createArray(mv, args.length, array_type);
//        //Now our array is on the top of stack.
//
//        int a_index = 0;
//        boolean has_large_local = false;
//
//        for (Type arg : args) {
//            if (arg.getSize() == 2) {
//                has_large_local = true;
//            }
//            mv.visitInsn(DUP);//Dup the array.
//            BytecodeUtil.pushInt(mv, a_index++);//Push current array index. Now the stack looks like [index,array,array]
//            mv.visitVarInsn(arg.getOpcode(ILOAD), locals);//Push the argument. Now the stack looks like [var,index,array,array]
//            int sort = arg.getSort();
//            if (sort >= Type.BOOLEAN && sort <= Type.DOUBLE) {
//                BytecodeUtil.boxing(mv,arg);
//            }
//            mv.visitInsn(array_type.getOpcode(IASTORE));//Store the argument into array. Now the stack looks like [array]
//            locals += arg.getSize();
//        }
//
//        //There should only be the array on the stack now.
//
//        mv.visitVarInsn(ALOAD, 0);//Push the MethodHandle. Now the stack looks like [handle,array]
//        mv.visitInsn(SWAP);//Ensure the order. Now the stack looks like [array,handle]
//        mv.visitMethodInsn(INVOKESTATIC, "alice/interceptor/MethodHandleInterceptor", name, "(Ljava/lang/invoke/MethodHandle;[Ljava/lang/Object;)Ljava/lang/Object;", false);
//
//        mv.visitMaxs(has_large_local ? 5 : 4, locals);
//    }
//
//    public static InvokeResult checkMethodHandle(MethodHandle mh, Object... args) throws Throwable {
//        MethodHandleInfo info = ReflectionUtil.resolve(mh);
//        if (info.getDeclaringClass() == MethodHandle.class) {
//            if (info.getName().equals("invoke")) {
//                assert args.length == 2;
//                mh = (MethodHandle) args[0];
//                args = (Object[]) args[1];
//                return new InvokeResult(invoke(mh, args), true);
//            } else if (info.getName().equals("invokeExact")) {
//                assert args.length == 2;
//                mh = (MethodHandle) args[0];
//                args = (Object[]) args[1];
//                return new InvokeResult(invokeExact(mh, args), true);
//            }
//        } else if (info.getDeclaringClass() == Method.class) {
//            if (info.getName().equals("invoke")) {
//                assert args.length == 3;
//                Method method = (Method) args[0];
//                Object obj = args[1];
//                args = (Object[]) args[2];
//                return new InvokeResult(ReflectionInterceptor.invoke(method, obj, args), true);
//            }
//        } else if (info.getDeclaringClass() == Field.class) {
//            assert args.length == 3;
//            Field field = (Field) args[0];
//            Object obj = args[1];
//            Object value = args[2];
//            ReflectionInterceptor.set(field, obj, value);
//            return new InvokeResult(null, true);
//        }
//
//        return new InvokeResult(null, false);
//    }
//
//    public static Object invoke(MethodHandle mh, Object... args) throws Throwable {
//        InvokeResult ir = checkMethodHandle(mh, args);
//        if (ir.success) {
//            return ir.ret;
//        }
//        return mh.invoke(args);
//    }
//
//    public static Object invokeExact(MethodHandle mh, Object... args) throws Throwable {
//        InvokeResult ir = checkMethodHandle(mh, args);
//        if (ir.success) {
//            return ir.ret;
//        }
//        return mh.invokeExact(args);
//    }
//
//    public static class InvokeResult {
//        private final Object ret;
//        private final boolean success;
//
//        private InvokeResult(Object ret, boolean success) {
//            this.ret = ret;
//            this.success = success;
//        }
//    }
}
