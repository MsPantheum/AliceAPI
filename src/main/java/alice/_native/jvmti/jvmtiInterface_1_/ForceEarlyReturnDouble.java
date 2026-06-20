package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ForceEarlyReturnDouble) (jvmtiEnv* env, jthread thread, jdouble value);
public final class ForceEarlyReturnDouble {

    private static final long code_base = JVMTINativeCall.create(ForceEarlyReturnDouble.class, "()I", JVMTINativeCall.FORCE_EARLY_RETURN_DOUBLE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.DOUBLE});

    private static native int holder();

    private ForceEarlyReturnDouble() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, double value) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, value);
        return holder();
    }

    public synchronized static int invoke(long thread, double value) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, value);
    }
}
