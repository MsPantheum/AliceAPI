package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *DestroyRawMonitor) (jvmtiEnv* env, jrawMonitorID monitor);
public final class DestroyRawMonitor {

    private DestroyRawMonitor() {
    }

    public static int invoke(long monitor) {
        return alice._native.jvmti.jvmtiInterface_1_.DestroyRawMonitor.invoke(JNIUtil.getJVMTIEnv(), monitor);
    }
}
