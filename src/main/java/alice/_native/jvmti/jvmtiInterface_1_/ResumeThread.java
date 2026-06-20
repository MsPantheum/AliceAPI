package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ResumeThread) (jvmtiEnv* env, jthread thread);
public final class ResumeThread {

    private static final long code_base = JVMTINativeCall.create(ResumeThread.class, "()I", JVMTINativeCall.RESUME_THREAD, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private ResumeThread() {
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
