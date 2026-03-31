package alice.test;

import alice.Meow;
import alice.api.ClassByteProcessor;
import alice.injector.patch.PatcherLoader;
import alice.util.URLClassPathWrapper;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.*;

public class TestPatcher {
    @Test
    public void test() {
        System.out.println("Start loading...");
        PatcherLoader.load();
        URLClassPathWrapper.registerProcessor(new ClassByteProcessor() {
            @Override
            public byte[] process(byte[] classBytes, String name) {
                if (name.equals("alice/Meow.class")) {
                    ClassReader cr = new ClassReader(classBytes);
                    ClassWriter cw = new ClassWriter(cr, 0);
                    ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
                        @Override
                        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {

                            if (name.equals("test") && descriptor.equals("()I")) {
                                MethodVisitor mv = super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, name, descriptor, signature, null);
                                mv.visitInsn(Opcodes.ICONST_1);
                                mv.visitInsn(Opcodes.IRETURN);
                                mv.visitMaxs(1, 0);
                                return null;
                            }

                            return super.visitMethod(access, name, descriptor, signature, exceptions);
                        }
                    };

                    cr.accept(cv, 0);
                    return cw.toByteArray();
                }
                return ClassByteProcessor.super.process(classBytes, name);
            }
        });
        assert Meow.test() == 1;
    }
}
