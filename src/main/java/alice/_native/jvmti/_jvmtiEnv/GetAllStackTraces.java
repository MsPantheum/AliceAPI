package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetAllStackTraces) (jvmtiEnv* env, jint max_frame_count, jvmtiStackInfo** stack_info_ptr, jint* thread_count_ptr);
public final class GetAllStackTraces {

    private GetAllStackTraces() {
    }

    public static int invoke(int max_frame_count, long stack_info_ptr, long thread_count_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetAllStackTraces.invoke(JNIUtil.getJVMTIEnv(), max_frame_count, stack_info_ptr, thread_count_ptr);
    }
}
