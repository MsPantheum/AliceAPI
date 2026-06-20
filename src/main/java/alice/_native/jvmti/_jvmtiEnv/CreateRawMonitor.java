package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *CreateRawMonitor) (jvmtiEnv* env, const char* name, jrawMonitorID* monitor_ptr);
public final class CreateRawMonitor {

    private CreateRawMonitor() {
    }

    public static int invoke(long name, long monitor_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.CreateRawMonitor.invoke(JNIUtil.getJVMTIEnv(), name, monitor_ptr);
    }
}
