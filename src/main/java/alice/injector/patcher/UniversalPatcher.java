package alice.injector.patcher;

import alice.Platform;
import alice.interceptor.ReflectionInterceptor;
import alice.util.BytecodeUtil;
import org.objectweb.asm.*;

import java.lang.reflect.Modifier;

public class UniversalPatcher implements Opcodes {

    public static byte[] patch(byte[] data, String class_name) {
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

                    int stack_require = -1;
                    int local_require = -1;

                    @Override
                    public void visitMaxs(int maxStack, int maxLocals) {
                        super.visitMaxs(Math.max(stack_require, maxStack), Math.max(local_require, maxLocals));
                    }

                    @Override
                    public void visitCode() {
                        super.visitCode();
                        if ((_name.equals("findClass") || _name.equals("loadClass")) && (_descriptor.startsWith("(Ljava/lang/String;") && _descriptor.endsWith(")Ljava/lang/Class;"))) {
                            int slot = Modifier.isStatic(_access) ? 0 : 1;
                            super.visitVarInsn(ALOAD, slot);
                            super.visitLdcInsn("alice");
                            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false);
                            Label alice = new Label();
                            super.visitJumpInsn(IFEQ, alice);
                            super.visitVarInsn(ALOAD, slot);
                            super.visitMethodInsn(INVOKESTATIC, "alice/interceptor/ClassLoaderInterceptor", "findClass", "(Ljava/lang/String;)Ljava/lang/Class;", false);
                            super.visitInsn(ARETURN);
                            super.visitLabel(alice);
                            super.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                            stack_require = 2;
                            changed[0] = true;
                        }
                    }

                    boolean first_special_insn = true;

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
                                if (first_special_insn) {
                                    super.visitVarInsn(ALOAD, 0);
                                }
                                super.visitMethodInsn(INVOKESTATIC, "alice/interceptor/URLClassLoaderInterceptor", "creation", "(Ljava/net/URLClassLoader;)Ljava/net/URLClassLoader;", false);
                                if (first_special_insn) {
                                    super.visitInsn(POP);
                                }
                                first_special_insn = false;
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
                                if (descriptor.startsWith("(Ljava/lang/invoke/MethodHandle;") || descriptor.startsWith("(Ljava/lang/reflect/")) {//Suspicious.
                                    changed[0] = true;
                                    opcode = INVOKESTATIC;
                                    owner = "alice/generated/".concat(BytecodeUtil.adapt(class_name.substring(0, class_name.length() - 6))).concat("_").concat(name).concat(BytecodeUtil.adapt(descriptor));
                                    descriptor = "(Ljava/lang/invoke/MethodHandle;".concat(descriptor.substring(1));
                                    ReflectionInterceptor.MethodHandleInterceptorProvider.addTarget(owner.concat(".class"), name, descriptor);
                                }
                            }
                        } else if (owner.equals("java/lang/module/ModuleReference") && name.equals("open") && descriptor.equals("()Ljava/lang/module/ModuleReader;")) {
                            opcode = INVOKESTATIC;
                            owner = "alice/interceptor/ModuleReferenceInterceptor";
                            descriptor = "(Ljava/lang/module/ModuleReference;)Ljava/lang/module/ModuleReader;";
                            changed[0] = true;
                        } else if (owner.equals("java/lang/ModuleLayer") && name.startsWith("defineModules")) {
                            changed[0] = true;
                            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                            boolean flag;
                            if (descriptor.endsWith("Ljava/lang/ModuleLayer;")) {
                                flag = true;
                            } else if (descriptor.endsWith("Ljava/lang/ModuleLayer$Controller;")) {
                                flag = false;
                            } else {
                                throw new IllegalStateException();
                            }
                            super.visitMethodInsn(INVOKESTATIC, "alice/interceptor/ModuleLayerInterceptor", flag ? "processModuleLayer" : "processController", flag ? "(Ljava/lang/ModuleLayer;)Ljava/lang/ModuleLayer;" : "(Ljava/lang/ModuleLayer$Controller;)Ljava/lang/ModuleLayer$Controller;", false);
                            return;
                        }
                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                        if (opcode == INVOKESPECIAL) {
                            first_special_insn = false;
                        }
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
