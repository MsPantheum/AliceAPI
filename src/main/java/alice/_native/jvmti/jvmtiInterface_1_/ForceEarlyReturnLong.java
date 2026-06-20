package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ForceEarlyReturnLong) (jvmtiEnv* env, jthread thread, jlong value);
public final class ForceEarlyReturnLong {

    private static final long code_base = JVMTINativeCall.create(ForceEarlyReturnLong.class, "()I", JVMTINativeCall.FORCE_EARLY_RETURN_LONG, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private ForceEarlyReturnLong() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, long value) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, value);
        return holder();
    }

    public synchronized static int invoke(long thread, long value) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, value);
    }
}
