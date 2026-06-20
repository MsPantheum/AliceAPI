package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetStackTrace) (jvmtiEnv* env, jthread thread, jint start_depth, jint max_frame_count, jvmtiFrameInfo* frame_buffer, jint* count_ptr);
public final class GetStackTrace {

    private static final long code_base = JVMTINativeCall.create(GetStackTrace.class, "()I", JVMTINativeCall.GET_STACK_TRACE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetStackTrace() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, int start_depth, int max_frame_count, long frame_buffer, long count_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, start_depth);
        JVMTINativeCall.setArg(code_base, 2, max_frame_count);
        JVMTINativeCall.setArg(code_base, 3, frame_buffer);
        JVMTINativeCall.setArg(code_base, 4, count_ptr);
        return holder();
    }

    public synchronized static int invoke(long thread, int start_depth, int max_frame_count, long frame_buffer, long count_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, start_depth, max_frame_count, frame_buffer, count_ptr);
    }
}
