package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *RawMonitorEnter) (jvmtiEnv* env, jrawMonitorID monitor);
public final class RawMonitorEnter {

    private RawMonitorEnter() {
    }

    public static int invoke(long monitor) {
        return alice._native.jvmti.jvmtiInterface_1_.RawMonitorEnter.invoke(JNIUtil.getJVMTIEnv(), monitor);
    }
}
