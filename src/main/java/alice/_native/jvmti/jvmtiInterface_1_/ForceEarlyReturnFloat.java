package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ForceEarlyReturnFloat) (jvmtiEnv* env, jthread thread, jfloat value);
public final class ForceEarlyReturnFloat {

    private static final long code_base = JVMTINativeCall.create(ForceEarlyReturnFloat.class, "()I", JVMTINativeCall.FORCE_EARLY_RETURN_FLOAT, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.FLOAT});

    private static native int holder();

    private ForceEarlyReturnFloat() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, float value) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, value);
        return holder();
    }

    public synchronized static int invoke(long thread, float value) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, value);
    }
}
