package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetOwnedMonitorStackDepthInfo) (jvmtiEnv* env, jthread thread, jint* monitor_info_count_ptr, jvmtiMonitorStackDepthInfo** monitor_info_ptr);
public final class GetOwnedMonitorStackDepthInfo {

    private GetOwnedMonitorStackDepthInfo() {
    }

    public static int invoke(long thread, long monitor_info_count_ptr, long monitor_info_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetOwnedMonitorStackDepthInfo.invoke(JNIUtil.getJVMTIEnv(), thread, monitor_info_count_ptr, monitor_info_ptr);
    }
}
