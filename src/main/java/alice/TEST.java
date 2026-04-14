package alice;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class TEST implements Opcodes {
    public static void main(String[] args) {

        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_8, ACC_PUBLIC, "alice/util/Unsafe", null, "java/lang/Object", null);
    }
}
