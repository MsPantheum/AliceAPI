package alice.injector.patcher;

import alice.Platform;
import alice.util.BytecodeUtil;
import org.objectweb.asm.*;

public class UniversalPatcher implements Opcodes {

    public static byte[] patch(byte[] data, String name) {
        if (data == null) {
            return null;
        }

        final boolean[] changed = {false};
        ClassReader cr = new ClassReader(data);

        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new ClassVisitor(Platform.ASM_LEVEL, cw) {
            @Override
            public MethodVisitor visitMethod(int _access, String _name, String _descriptor, String _signature, String[] _exceptions) {
                return new MethodVisitor(Platform.ASM_LEVEL, cv.visitMethod(_access, _name, _descriptor, _signature, _exceptions)) {
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                        if (owner.equals("sun/misc/Unsafe")) {
                            if (name.equals("putInt") || name.equals("putIntVolatile")) {
                                changed[0] = true;
                                opcode = INVOKESTATIC;
                                owner = "alice/interceptor/UnsafeInterceptor";
                                descriptor = "(Lsun/misc/Unsafe;" + descriptor.substring(1);
                            }
                        } else if (opcode == INVOKESPECIAL && _name.equals("<init>") && name.equals("<init>")) {
                            if (owner.equals("java/net/URLClassLoader") || owner.equals("sun/applet/AppletClassLoader")) {
                                changed[0] = true;
                                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                                super.visitVarInsn(ALOAD, 0);
                                super.visitMethodInsn(INVOKESTATIC, "alice/interceptor/URLClassLoaderInterceptor", "creation", "(Ljava/net/URLClassLoader;)Ljava/net/URLClassLoader;", false);
                                super.visitInsn(POP);
                                return;
                            }
                        } else if (owner.equals("java/lang/reflect/Method")) {
                            if (name.equals("invoke")) {
                                changed[0] = true;
                                opcode = INVOKESTATIC;
                                owner = "alice/interceptor/ReflectionInterceptor";
                                descriptor = "(Ljava/lang/reflect/Method;" + descriptor.substring(1);
                            }
                        } else if (owner.equals("java/lang/reflect/Field")) {
                            if (name.equals("set")) {
                                changed[0] = true;
                                opcode = INVOKESTATIC;
                                owner = "alice/interceptor/ReflectionInterceptor";
                                descriptor = "(Ljava/lang/reflect/Field;" + descriptor.substring(1);
                            }
                        } else if (owner.equals("java/lang/invoke/MethodHandle")) {
                            if (name.equals("invoke") || name.equals("invokeExact")) {
                                Type type = Type.getType(descriptor);
                                Type[] args = type.getArgumentTypes();
                                Type ret_type = type.getReturnType();
                                if (descriptor.startsWith("(Ljava/lang/invoke/MethodHandle;") || descriptor.startsWith("(Ljava/lang/reflect/")) {//What are you hiding?
                                    changed[0] = true;
                                    BytecodeUtil.popVariables(mv, args);
                                    super.visitInsn(POP);//The "this" is on top of stack after all the other things cleared.
                                    BytecodeUtil.generateValue(mv, ret_type);
                                    return;
                                }
                            }
                        }
                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    }

                    @Override
                    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                        super.visitFieldInsn(opcode, owner, name, descriptor);
                    }

                };
            }
        };

        cr.accept(cv, 0);
        byte[] ret = cw.toByteArray();
        return changed[0] ? ret : data;
    }
}
