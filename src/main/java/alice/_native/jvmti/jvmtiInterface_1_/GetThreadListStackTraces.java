package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetThreadListStackTraces) (jvmtiEnv* env, jint thread_count, const jthread* thread_list, jint max_frame_count, jvmtiStackInfo** stack_info_ptr);
public final class GetThreadListStackTraces {

    private static final long code_base = JVMTINativeCall.create(GetThreadListStackTraces.class, "()I", JVMTINativeCall.GET_THREAD_LIST_STACK_TRACES, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetThreadListStackTraces() {
    }

    public synchronized static int invoke(long JVMTIEnv, int thread_count, long thread_list, int max_frame_count, long stack_info_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread_count);
        JVMTINativeCall.setArg(code_base, 1, thread_list);
        JVMTINativeCall.setArg(code_base, 2, max_frame_count);
        JVMTINativeCall.setArg(code_base, 3, stack_info_ptr);
        return holder();
    }

    public synchronized static int invoke(int thread_count, long thread_list, int max_frame_count, long stack_info_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), thread_count, thread_list, max_frame_count, stack_info_ptr);
    }
}
