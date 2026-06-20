package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *RawMonitorWait) (jvmtiEnv* env, jrawMonitorID monitor, jlong millis);
public final class RawMonitorWait {

    private RawMonitorWait() {
    }

    public static int invoke(long monitor, long millis) {
        return alice._native.jvmti.jvmtiInterface_1_.RawMonitorWait.invoke(JNIUtil.getJVMTIEnv(), monitor, millis);
    }
}
