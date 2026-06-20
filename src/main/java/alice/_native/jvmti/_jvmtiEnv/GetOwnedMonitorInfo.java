package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetOwnedMonitorInfo) (jvmtiEnv* env, jthread thread, jint* owned_monitor_count_ptr, jobject** owned_monitors_ptr);
public final class GetOwnedMonitorInfo {

    private GetOwnedMonitorInfo() {
    }

    public static int invoke(long thread, long owned_monitor_count_ptr, long owned_monitors_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetOwnedMonitorInfo.invoke(JNIUtil.getJVMTIEnv(), thread, owned_monitor_count_ptr, owned_monitors_ptr);
    }
}
