package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetTimerInfo) (jvmtiEnv* env, jvmtiTimerInfo* info_ptr);
public final class GetTimerInfo {

    private GetTimerInfo() {
    }

    public static int invoke(long info_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetTimerInfo.invoke(JNIUtil.getJVMTIEnv(), info_ptr);
    }
}
