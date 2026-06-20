package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetThreadInfo) (jvmtiEnv* env, jthread thread, jvmtiThreadInfo* info_ptr);
public final class GetThreadInfo {

    private GetThreadInfo() {
    }

    public static int invoke(long thread, long info_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetThreadInfo.invoke(JNIUtil.getJVMTIEnv(), thread, info_ptr);
    }
}
