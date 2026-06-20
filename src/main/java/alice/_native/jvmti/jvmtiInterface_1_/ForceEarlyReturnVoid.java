package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ForceEarlyReturnVoid) (jvmtiEnv* env, jthread thread);
public final class ForceEarlyReturnVoid {

    private static final long code_base = JVMTINativeCall.create(ForceEarlyReturnVoid.class, "()I", JVMTINativeCall.FORCE_EARLY_RETURN_VOID, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private ForceEarlyReturnVoid() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        return holder();
    }

    public synchronized static int invoke(long thread) {
        return invoke(JNIUtil.getJVMTIEnv(), thread);
    }
}
