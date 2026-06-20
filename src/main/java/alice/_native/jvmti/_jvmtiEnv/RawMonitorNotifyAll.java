package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *RawMonitorNotifyAll) (jvmtiEnv* env, jrawMonitorID monitor);
public final class RawMonitorNotifyAll {

    private RawMonitorNotifyAll() {
    }

    public static int invoke(long monitor) {
        return alice._native.jvmti.jvmtiInterface_1_.RawMonitorNotifyAll.invoke(JNIUtil.getJVMTIEnv(), monitor);
    }
}
