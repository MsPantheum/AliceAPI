package alice.injector.patch;

import org.objectweb.asm.*;

public class UniversalPatcher implements Opcodes {
    public static byte[] patch(byte[] data, String name) {
        final boolean[] changed = {false};
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return new MethodVisitor(ASM5) {
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                        if (owner.equals("sun/misc/Unsafe")) {
                            if (name.equals("putInt") && descriptor.equals("(JI)V")) {
                                changed[0] = true;
                                opcode = INVOKESTATIC;
                                owner = "alice/interceptor/UnsafeInterceptor";
                            }
                        }
                        if (name.equals("<init>")) {
                            String _super = cr.getSuperName();
                            if (_super.equals("java/net/URLClassLoader") || _super.equals("sun/applet/AppletClassLoader")) {
                                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                                super.visitMethodInsn(INVOKESTATIC, "alice/interceptor/URLClassLoaderInterceptor", "creation", "(Ljava/net/URLClassLoader;)Ljava/net/URLClassLoader;", false);
                                return;
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
        return changed[0] ? cw.toByteArray() : data;
    }
}
