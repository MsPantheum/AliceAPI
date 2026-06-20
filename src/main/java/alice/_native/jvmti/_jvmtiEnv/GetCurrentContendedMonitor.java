package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetCurrentContendedMonitor) (jvmtiEnv* env, jthread thread, jobject* monitor_ptr);
public final class GetCurrentContendedMonitor {

    private GetCurrentContendedMonitor() {
    }

    public static int invoke(long thread, long monitor_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetCurrentContendedMonitor.invoke(JNIUtil.getJVMTIEnv(), thread, monitor_ptr);
    }
}
