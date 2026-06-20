package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetAllThreads) (jvmtiEnv* env, jint* threads_count_ptr, jthread** threads_ptr);
public final class GetAllThreads {

    private static final long code_base = JVMTINativeCall.create(GetAllThreads.class, "()I", JVMTINativeCall.GET_ALL_THREADS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetAllThreads() {
    }

    public synchronized static int invoke(long JVMTIEnv, long threads_count_ptr, long threads_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, threads_count_ptr);
        JVMTINativeCall.setArg(code_base, 1, threads_ptr);
        return holder();
    }

    public synchronized static int invoke(long threads_count_ptr, long threads_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), threads_count_ptr, threads_ptr);
    }
}
