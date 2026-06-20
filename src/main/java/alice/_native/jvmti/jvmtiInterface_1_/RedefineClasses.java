package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *RedefineClasses) (jvmtiEnv* env, jint class_count, const jvmtiClassDefinition* class_definitions);
public final class RedefineClasses {

    private static final long code_base = JVMTINativeCall.create(RedefineClasses.class, "()I", JVMTINativeCall.REDEFINE_CLASSES, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private RedefineClasses() {
    }

    public synchronized static int invoke(long JVMTIEnv, int class_count, long class_definitions) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, class_count);
        JVMTINativeCall.setArg(code_base, 1, class_definitions);
        return holder();
    }

    public synchronized static int invoke(int class_count, long class_definitions) {
        return invoke(JNIUtil.getJVMTIEnv(), class_count, class_definitions);
    }
}
