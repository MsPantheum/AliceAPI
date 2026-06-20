package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetStackTrace) (jvmtiEnv* env, jthread thread, jint start_depth, jint max_frame_count, jvmtiFrameInfo* frame_buffer, jint* count_ptr);
public final class GetStackTrace {

    private GetStackTrace() {
    }

    public static int invoke(long thread, int start_depth, int max_frame_count, long frame_buffer, long count_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetStackTrace.invoke(JNIUtil.getJVMTIEnv(), thread, start_depth, max_frame_count, frame_buffer, count_ptr);
    }
}
