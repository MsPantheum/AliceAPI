package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetThreadCpuTimerInfo) (jvmtiEnv* env, jvmtiTimerInfo* info_ptr);
public final class GetThreadCpuTimerInfo {

    private GetThreadCpuTimerInfo() {
    }

    public static int invoke(long info_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetThreadCpuTimerInfo.invoke(JNIUtil.getJVMTIEnv(), info_ptr);
    }
}
