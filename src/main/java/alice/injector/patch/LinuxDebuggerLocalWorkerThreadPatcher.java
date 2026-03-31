package alice.injector.patch;

import org.objectweb.asm.*;

public class LinuxDebuggerLocalWorkerThreadPatcher {

    //No need to do things on an attach thread as we are not really attached but in the same process.

    public static byte[] patch(byte[] data, String name) {
        System.out.println("Patching " + name + ".");
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                switch (name) {
                    case "run": {
                        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                        mv.visitInsn(Opcodes.RETURN);
                        mv.visitMaxs(0, 1);
                        return null;
                    }
                    case "execute": {
                        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                        mv.visitVarInsn(Opcodes.ALOAD, 1);
                        mv.visitInsn(Opcodes.DUP);
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitFieldInsn(Opcodes.GETFIELD,"sun/jvm/hotspot/debugger/linux/LinuxDebuggerLocal$LinuxDebuggerLocalWorkerThread","debugger","Lsun/jvm/hotspot/debugger/linux/LinuxDebuggerLocal;");
                        mv.visitMethodInsn(Opcodes.INVOKEINTERFACE,"sun/jvm/hotspot/debugger/linux/LinuxDebuggerLocal$WorkerThreadTask","doit","(Lsun/jvm/hotspot/debugger/linux/LinuxDebuggerLocal;)V",true);
                        mv.visitInsn(Opcodes.ARETURN);
                        mv.visitMaxs(3,2);
                        return null;
                    }
                }
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        };

        cr.accept(cv, 0);
        return cw.toByteArray();
    }
}
