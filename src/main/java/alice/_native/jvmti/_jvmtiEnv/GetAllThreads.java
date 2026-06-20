package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetAllThreads) (jvmtiEnv* env, jint* threads_count_ptr, jthread** threads_ptr);
public final class GetAllThreads {

    private GetAllThreads() {
    }

    public static int invoke(long threads_count_ptr, long threads_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetAllThreads.invoke(JNIUtil.getJVMTIEnv(), threads_count_ptr, threads_ptr);
    }
}
