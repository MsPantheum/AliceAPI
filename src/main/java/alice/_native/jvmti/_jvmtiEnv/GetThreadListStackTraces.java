package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetThreadListStackTraces) (jvmtiEnv* env, jint thread_count, const jthread* thread_list, jint max_frame_count, jvmtiStackInfo** stack_info_ptr);
public final class GetThreadListStackTraces {

    private GetThreadListStackTraces() {
    }

    public static int invoke(int thread_count, long thread_list, int max_frame_count, long stack_info_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetThreadListStackTraces.invoke(JNIUtil.getJVMTIEnv(), thread_count, thread_list, max_frame_count, stack_info_ptr);
    }
}
