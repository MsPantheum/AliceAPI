package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetEventNotificationMode) (jvmtiEnv* env, jvmtiEventMode mode, jvmtiEvent event_type, jthread event_thread);
public final class SetEventNotificationMode {

    private SetEventNotificationMode() {
    }

    public static int invoke(int mode, int event_type, long event_thread) {
        return alice._native.jvmti.jvmtiInterface_1_.SetEventNotificationMode.invoke(JNIUtil.getJVMTIEnv(), mode, event_type, event_thread);
    }
}
