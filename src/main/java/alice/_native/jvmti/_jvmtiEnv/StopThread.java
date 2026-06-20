package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *StopThread) (jvmtiEnv* env, jthread thread, jobject exception);
public final class StopThread {

    private StopThread() {
    }

    public static int invoke(long thread, long exception) {
        return alice._native.jvmti.jvmtiInterface_1_.StopThread.invoke(JNIUtil.getJVMTIEnv(), thread, exception);
    }
}
