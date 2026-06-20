package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ResumeThread) (jvmtiEnv* env, jthread thread);
public final class ResumeThread {

    private ResumeThread() {
    }

    public static int invoke(long thread) {
        return alice._native.jvmti.jvmtiInterface_1_.ResumeThread.invoke(JNIUtil.getJVMTIEnv(), thread);
    }
}
