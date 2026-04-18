package alice.injector.patcher;

import alice.Platform;
import alice.util.BytecodeUtil;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DebuggerBasePatcher implements Opcodes {
    public static byte[] transform(byte[] classBytes) {
        return BytecodeUtil.patchClass(classBytes, cw -> new ClassVisitor(Platform.ASM_LEVEL, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                if ("initCache".equals(name)) {
                    mv.visitInsn(RETURN);
                    mv.visitMaxs(0, 5);
                    return null;
                }
                return mv;
            }
        });
    }
}
