package alice.injector;

import alice.Platform;
import alice.util.ClassUtil;
import alice.util.FileUtil;
import alice.util.ReflectionUtil;
import alice.util.Unsafe;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.BiFunction;

public class Overrider implements Opcodes {

    public interface MethodProcessor {
        default boolean shouldCallSuper() {
            return true;
        }

        default int appendStack() {
            return 0;
        }

        default int appendLocal() {
            return 0;
        }

        void accept(MethodVisitor mv);
    }

    public interface ClassProcessor {
        void accept(ClassWriter cw);
    }

    public static Class<?> override(Class<?> target, BiFunction<String, String, MethodProcessor> mpProvider) {
        return override(target, mpProvider, null);
    }

    public static Class<?> override(Class<?> target, BiFunction<String, String, MethodProcessor> mpProvider, ClassProcessor cp) {
        if (Modifier.isAbstract(target.getModifiers())) {
            throw new IllegalArgumentException();
        }
        Method[] methods = ReflectionUtil.getMethods(target);
        ClassWriter cw = new ClassWriter(0);
        //boolean inner = target.getDeclaringClass() != null;
        String super_name = target.getName().replace(".", "/");
        String super_type = 'L' + super_name + ';';
        String c_name = super_name + "Overrides";
        cw.visit(V1_8, ACC_PUBLIC, c_name, null, super_name, null);
        cw.visitField(ACC_PRIVATE | ACC_FINAL, "delegate", super_type, null, null);
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(" + super_type + ")V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(PUTFIELD, c_name, "delegate", super_type);
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
        for (Method method : methods) {
            if (!Modifier.isPrivate(method.getModifiers()) && !Modifier.isStatic(method.getModifiers())) {

                Type type = Type.getType(method);
                String m_name = method.getName();
                String desc = type.getDescriptor();

                Class<?>[] exceptions = method.getExceptionTypes();
                String[] exception_types;
                if (exceptions.length != 0) {
                    exception_types = new String[exceptions.length];
                    for (int i = 0; i < exceptions.length; i++) {
                        exception_types[i] = exceptions[i].getName().replace(".", "/");
                    }
                } else {
                    exception_types = null;
                }
                Type ret_type = type.getReturnType();
                Type[] args = type.getArgumentTypes();
                mv = cw.visitMethod(method.getModifiers(), m_name, desc, ReflectionUtil.getGenericSignature(method), exception_types);
                mv.visitCode();
                int index = 0;
                int maxLocal = 1;
                for (Type arg : args) {
                    maxLocal += arg.getSize();
                }
                MethodProcessor processor = mpProvider.apply(m_name, desc);

                if (processor == null || processor.shouldCallSuper()) {
                    mv.visitVarInsn(ALOAD, index++);
                    mv.visitFieldInsn(GETFIELD, c_name, "delegate", super_type);
                    for (Type arg : args) {
                        mv.visitVarInsn(arg.getOpcode(ILOAD), index);
                        index += arg.getSize();
                    }
                    mv.visitMethodInsn(INVOKEVIRTUAL, super_name, m_name, desc, false);
                }

                if (processor != null) {
                    processor.accept(mv);
                }

                mv.visitInsn(ret_type.getOpcode(IRETURN));
                mv.visitMaxs(index + (processor != null ? processor.appendStack() : 0), maxLocal + (processor != null ? processor.appendLocal() : 0));
                mv.visitEnd();
            }
        }
        if (cp != null) {
            cp.accept(cw);
        }
        cw.visitEnd();
        byte[] b = cw.toByteArray();
        FileUtil.write("Dump/" + c_name + ".class", b);
        return Platform.jigsaw ? ClassUtil.defineClass1(target.getClassLoader(), target.getName() + "Overrides", b, 0, b.length, target.getProtectionDomain(), "null") : Unsafe.defineClass(target.getName() + "Overrides", b, 0, b.length, target.getClassLoader(), target.getProtectionDomain());
    }
}
