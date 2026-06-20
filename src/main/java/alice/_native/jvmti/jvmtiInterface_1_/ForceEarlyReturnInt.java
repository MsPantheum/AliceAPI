package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ForceEarlyReturnInt) (jvmtiEnv* env, jthread thread, jint value);
public final class ForceEarlyReturnInt {

    private static final long code_base = JVMTINativeCall.create(ForceEarlyReturnInt.class, "()I", JVMTINativeCall.FORCE_EARLY_RETURN_INT, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private ForceEarlyReturnInt() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, int value) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, value);
        return holder();
    }

    public synchronized static int invoke(long thread, int value) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, value);
    }
}
