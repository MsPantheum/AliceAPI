package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetCurrentThread) (jvmtiEnv* env, jthread* thread_ptr);
public final class GetCurrentThread {

    private GetCurrentThread() {
    }

    public static int invoke(long thread_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetCurrentThread.invoke(JNIUtil.getJVMTIEnv(), thread_ptr);
    }
}
