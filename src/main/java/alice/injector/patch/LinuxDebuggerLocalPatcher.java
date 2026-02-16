package alice.injector.patch;

import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import org.objectweb.asm.*;
import org.objectweb.asm.Opcodes.*;

public class LinuxDebuggerLocalPatcher {
    public static byte[] patch(byte[] data){
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(cr,0);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5,cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {

                switch (name) {
                    case "detach0":
                    case "attach0": {
                        MethodVisitor mv = super.visitMethod(Opcodes.ACC_PRIVATE, name, descriptor, signature, exceptions);
                        mv.visitInsn(Opcodes.RETURN);
                        return null;
                    }//Don't need to attach as we are in the same process.
                    case "getCDebugger": {
                        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                        mv.visitInsn(Opcodes.ACONST_NULL);
                        mv.visitInsn(Opcodes.ARETURN);
                        return null;
                    }//No we won't implement this.

                }
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        };

        cr.accept(cv,0);
        return cw.toByteArray();
    }
}
