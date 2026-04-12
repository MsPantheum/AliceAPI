package alice.util;

import org.objectweb.asm.*;

import java.util.function.Function;

public class BytecodeUtil implements Opcodes {

    /**
     * Patch a class with the given ClassVisitor.
     *
     * @param original   the original class bytes
     * @param cvFunction provide your ClassVisitor, you should pass the ClassWriter into its constructor.
     * @return patched class bytes
     */
    public static byte[] patchClass(byte[] original, Function<ClassWriter, ClassVisitor> cvFunction) {
        ClassReader cr = new ClassReader(original);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = cvFunction.apply(cw);
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    /**
     * Generate a println invoke which prints a given string.
     *
     * @param mv      the method to inject
     * @param message the str to print
     */
    public static void println(MethodVisitor mv, String message) {
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn(message);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }

    /**
     * Generate a println invoke which prints a object read from local variables.
     *
     * @param mv    the method to inject
     * @param index the index of the local variable
     */
    public static void println(MethodVisitor mv, int index) {
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitVarInsn(ALOAD, index);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
    }
}
