package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetThreadState) (jvmtiEnv* env, jthread thread, jint* thread_state_ptr);
public final class GetThreadState {

    private GetThreadState() {
    }

    public static int invoke(long thread, long thread_state_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetThreadState.invoke(JNIUtil.getJVMTIEnv(), thread, thread_state_ptr);
    }
}
