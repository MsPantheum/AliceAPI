package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetCurrentThreadCpuTimerInfo) (jvmtiEnv* env, jvmtiTimerInfo* info_ptr);
public final class GetCurrentThreadCpuTimerInfo {

    private GetCurrentThreadCpuTimerInfo() {
    }

    public static int invoke(long info_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetCurrentThreadCpuTimerInfo.invoke(JNIUtil.getJVMTIEnv(), info_ptr);
    }
}
