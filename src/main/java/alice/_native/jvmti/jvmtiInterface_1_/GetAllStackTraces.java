package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetAllStackTraces) (jvmtiEnv* env, jint max_frame_count, jvmtiStackInfo** stack_info_ptr, jint* thread_count_ptr);
public final class GetAllStackTraces {

    private static final long code_base = JVMTINativeCall.create(GetAllStackTraces.class, "()I", JVMTINativeCall.GET_ALL_STACK_TRACES, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetAllStackTraces() {
    }

    public synchronized static int invoke(long JVMTIEnv, int max_frame_count, long stack_info_ptr, long thread_count_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, max_frame_count);
        JVMTINativeCall.setArg(code_base, 1, stack_info_ptr);
        JVMTINativeCall.setArg(code_base, 2, thread_count_ptr);
        return holder();
    }

    public synchronized static int invoke(int max_frame_count, long stack_info_ptr, long thread_count_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), max_frame_count, stack_info_ptr, thread_count_ptr);
    }
}
