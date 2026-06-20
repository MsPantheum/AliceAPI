package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetThreadState) (jvmtiEnv* env, jthread thread, jint* thread_state_ptr);
public final class GetThreadState {

    private static final long code_base = JVMTINativeCall.create(GetThreadState.class, "()I", JVMTINativeCall.GET_THREAD_STATE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetThreadState() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, long thread_state_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, thread_state_ptr);
        return holder();
    }

    public synchronized static int invoke(long thread, long thread_state_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, thread_state_ptr);
    }
}
