package alice;

import alice._native.jni.JNINativeInterface_.FindClass;
import alice._native.jvmti.jvmtiCapabilities;
import alice._native.jvmti.jvmtiClassDefinition;
import alice._native.jvmti.jvmtiInterface_1_.AddCapabilities;
import alice._native.jvmti.jvmtiInterface_1_.RedefineClasses;
import alice.exception.JNIException;
import alice.util.BytecodeUtil;
import alice.util.ClassUtil;
import alice.util.Unsafe;

import static org.objectweb.asm.Opcodes.ICONST_3;
import static org.objectweb.asm.Opcodes.IRETURN;

public class Test8 {
    public static void main(String[] args) {
        System.out.println("Before redefinition: ".concat(String.valueOf(Meow.test())));
        Init.ensureInit();
        jvmtiCapabilities caps = new jvmtiCapabilities();
        caps.clear();
        caps.can_redefine_classes(true);
        int ret = AddCapabilities.invoke(caps.address);
        if (ret != 0) {
            throw new JNIException("Failed to add JVMTI capabilities!", ret);
        }
        byte[] raw_class = ClassUtil.dumpFromMemory(Meow.class);
        byte[] patched = BytecodeUtil.patchMethod(raw_class, "test()I", mv -> {
            mv.visitInsn(ICONST_3);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(1, 0);
            return null;
        });
        int class_byte_count = patched.length;
        long class_bytes = Unsafe.allocateMemory(class_byte_count);
        jvmtiClassDefinition jvmtiClassDefinition = new jvmtiClassDefinition();
        try {
            Unsafe.writeBytes(class_bytes, patched);
            jvmtiClassDefinition.klass(FindClass.invoke("alice/Meow"));
            jvmtiClassDefinition.class_byte_count(class_byte_count);
            jvmtiClassDefinition.class_bytes(class_bytes);
            ret = RedefineClasses.invoke(1, jvmtiClassDefinition.address);
            if (ret != 0) {
                throw new JNIException("Failed to redefine class!", ret);
            }
        } finally {
            Unsafe.freeMemory(class_bytes);
            jvmtiClassDefinition.release();
            caps.release();
        }
        System.out.println("After redefinition: ".concat(String.valueOf(Meow.test())));
    }
}
