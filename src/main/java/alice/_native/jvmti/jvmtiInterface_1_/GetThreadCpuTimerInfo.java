package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetThreadCpuTimerInfo) (jvmtiEnv* env, jvmtiTimerInfo* info_ptr);
public final class GetThreadCpuTimerInfo {

    private static final long code_base = JVMTINativeCall.create(GetThreadCpuTimerInfo.class, "()I", JVMTINativeCall.GET_THREAD_CPU_TIMER_INFO, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetThreadCpuTimerInfo() {
    }

    public synchronized static int invoke(long JVMTIEnv, long info_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, info_ptr);
        return holder();
    }

    public synchronized static int invoke(long info_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), info_ptr);
    }
}
