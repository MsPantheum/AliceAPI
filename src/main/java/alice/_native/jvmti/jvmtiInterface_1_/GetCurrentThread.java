package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetCurrentThread) (jvmtiEnv* env, jthread* thread_ptr);
public final class GetCurrentThread {

    private static final long code_base = JVMTINativeCall.create(GetCurrentThread.class, "()I", JVMTINativeCall.GET_CURRENT_THREAD, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetCurrentThread() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread_ptr);
        return holder();
    }

    public synchronized static int invoke(long thread_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), thread_ptr);
    }
}
