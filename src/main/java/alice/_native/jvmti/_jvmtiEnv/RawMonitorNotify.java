package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *RawMonitorNotify) (jvmtiEnv* env, jrawMonitorID monitor);
public final class RawMonitorNotify {

    private RawMonitorNotify() {
    }

    public static int invoke(long monitor) {
        return alice._native.jvmti.jvmtiInterface_1_.RawMonitorNotify.invoke(JNIUtil.getJVMTIEnv(), monitor);
    }
}
