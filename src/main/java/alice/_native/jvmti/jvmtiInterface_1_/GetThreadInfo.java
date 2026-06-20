package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetThreadInfo) (jvmtiEnv* env, jthread thread, jvmtiThreadInfo* info_ptr);
public final class GetThreadInfo {

    private static final long code_base = JVMTINativeCall.create(GetThreadInfo.class, "()I", JVMTINativeCall.GET_THREAD_INFO, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetThreadInfo() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, long info_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, info_ptr);
        return holder();
    }

    public synchronized static int invoke(long thread, long info_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, info_ptr);
    }
}
