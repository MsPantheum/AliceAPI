package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *RawMonitorExit) (jvmtiEnv* env, jrawMonitorID monitor);
public final class RawMonitorExit {

    private RawMonitorExit() {
    }

    public static int invoke(long monitor) {
        return alice._native.jvmti.jvmtiInterface_1_.RawMonitorExit.invoke(JNIUtil.getJVMTIEnv(), monitor);
    }
}
