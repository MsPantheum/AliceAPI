package alice.injector.patch;

import alice.util.FileUtil;
import alice.util.Unsafe;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
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
                    case "detach0": {
                        MethodVisitor mv = super.visitMethod(Opcodes.ACC_PRIVATE, name, descriptor, signature, null);

                        mv.visitInsn(Opcodes.RETURN);
                        mv.visitMaxs(0,1);
                        return null;
                    }
                    case "attach0": {
                        MethodVisitor mv = super.visitMethod(Opcodes.ACC_PRIVATE, name, descriptor, signature, exceptions);
                        mv.visitInsn(Opcodes.RETURN);
                        mv.visitMaxs(0,descriptor.contains("L") ? 3 : 2);
                        return null;
                    }//Don't need to attach as we are in the same process.
                    case "getCDebugger": {
                        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                        mv.visitInsn(Opcodes.ACONST_NULL);
                        mv.visitInsn(Opcodes.ARETURN);
                        mv.visitMaxs(1,1);
                        return null;
                    }//No we won't implement this.
                    case "getAddressSize": {
                        MethodVisitor mv = super.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, name, descriptor, signature, exceptions);
                        int size = Unsafe.ADDRESS_SIZE;
                        if(size == 4){
                            mv.visitInsn(Opcodes.ICONST_4);
                        } else {
                            mv.visitLdcInsn(size);
                        }
                        mv.visitInsn(Opcodes.IRETURN);
                        mv.visitMaxs(1,1);
                        return null;
                    }
                    case "lookupByName0": {
                        MethodVisitor mv = super.visitMethod(Opcodes.ACC_PRIVATE, name, descriptor, signature, exceptions);
                        mv.visitVarInsn(Opcodes.ALOAD,1);
                        mv.visitVarInsn(Opcodes.ALOAD,2);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC,"alice/injector/SymbolLookup","lookup","(Ljava/lang/String;Ljava/lang/String;)J",false);
                        mv.visitInsn(Opcodes.LRETURN);
                        mv.visitMaxs(2,3);
                        return null;
                    }
                    case "readBytesFromProcess0": {
                        MethodVisitor mv = super.visitMethod(Opcodes.ACC_PRIVATE, name, descriptor, signature, exceptions);
                        mv.visitVarInsn(Opcodes.LLOAD,1);
                        mv.visitVarInsn(Opcodes.LLOAD,3);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC,"alice/util/Unsafe","readBytes","(JJ)[B",false);
                        mv.visitInsn(Opcodes.ARETURN);
                        mv.visitMaxs(4,5);
                        return null;
                    }
                }
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        };

        cr.accept(cv,0);
        byte[] ret = cw.toByteArray();
        FileUtil.write("LinuxDebuggerLocal.class",ret);
        return ret;
    }
}
