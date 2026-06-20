package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetEventNotificationMode) (jvmtiEnv* env, jvmtiEventMode mode, jvmtiEvent event_type, jthread event_thread);
public final class SetEventNotificationMode {

    private static final long code_base = JVMTINativeCall.create(SetEventNotificationMode.class, "()I", JVMTINativeCall.SET_EVENT_NOTIFICATION_MODE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private SetEventNotificationMode() {
    }

    public synchronized static int invoke(long JVMTIEnv, int mode, int event_type, long event_thread) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, mode);
        JVMTINativeCall.setArg(code_base, 1, event_type);
        JVMTINativeCall.setArg(code_base, 2, event_thread);
        return holder();
    }

    public synchronized static int invoke(int mode, int event_type, long event_thread) {
        return invoke(JNIUtil.getJVMTIEnv(), mode, event_type, event_thread);
    }
}
