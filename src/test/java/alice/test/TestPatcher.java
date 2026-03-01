package alice.test;

import alice.Meow;
import alice.api.ClassByteProcessor;
import alice.injector.patch.PatcherLoader;
import alice.util.URLClassPathWrapper;
import alice.util.Unsafe;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.*;
import sun.jvm.hotspot.debugger.MachineDescriptionAMD64;
import sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.fail;

public class TestPatcher {
    @Test
    public void test(){
        System.out.println("Start loading...");
        PatcherLoader.load();
        URLClassPathWrapper.registerProcessor(new ClassByteProcessor() {
            @Override
            public byte[] process(String name, byte[] classBytes) {
                if(name.equals("alice.Meow")){
                    ClassReader cr = new ClassReader(classBytes);
                    ClassWriter cw = new ClassWriter(cr,0);
                    ClassVisitor cv = new ClassVisitor(Opcodes.ASM5,cw) {
                        @Override
                        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {

                            if(name.equals("test") && descriptor.equals("()V")){
                                MethodVisitor mv = super.visitMethod(Opcodes.ACC_PRIVATE, name, descriptor, signature, null);
                                mv.visitInsn(Opcodes.ICONST_1);
                                mv.visitInsn(Opcodes.IRETURN);
                                mv.visitMaxs(1,0);
                                return null;
                            }

                            return super.visitMethod(access, name, descriptor, signature, exceptions);
                        }
                    };

                    cr.accept(cv,0);
                    return cw.toByteArray();
                }
                return ClassByteProcessor.super.process(name, classBytes);
            }
        });
        System.out.println("Loading completed.");
        if(Meow.test() == 1){
            System.out.println("Test passed.");
        } else {
            fail("Test failed.");
        }
    }
}
