package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *InterruptThread) (jvmtiEnv* env, jthread thread);
public final class InterruptThread {

    private InterruptThread() {
    }

    public static int invoke(long thread) {
        return alice._native.jvmti.jvmtiInterface_1_.InterruptThread.invoke(JNIUtil.getJVMTIEnv(), thread);
    }
}
