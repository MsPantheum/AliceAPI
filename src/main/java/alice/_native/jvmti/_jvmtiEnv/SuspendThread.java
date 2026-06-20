package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SuspendThread) (jvmtiEnv* env, jthread thread);
public final class SuspendThread {

    private SuspendThread() {
    }

    public static int invoke(long thread) {
        return alice._native.jvmti.jvmtiInterface_1_.SuspendThread.invoke(JNIUtil.getJVMTIEnv(), thread);
    }
}
