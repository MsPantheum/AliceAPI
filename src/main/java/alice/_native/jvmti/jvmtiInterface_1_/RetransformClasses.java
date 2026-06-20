package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *RetransformClasses) (jvmtiEnv* env, jint class_count, const jclass* classes);
public final class RetransformClasses {

    private static final long code_base = JVMTINativeCall.create(RetransformClasses.class, "()I", JVMTINativeCall.RETRANSFORM_CLASSES, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private RetransformClasses() {
    }

    public synchronized static int invoke(long JVMTIEnv, int class_count, long classes) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, class_count);
        JVMTINativeCall.setArg(code_base, 1, classes);
        return holder();
    }

    public synchronized static int invoke(int class_count, long classes) {
        return invoke(JNIUtil.getJVMTIEnv(), class_count, classes);
    }
}
