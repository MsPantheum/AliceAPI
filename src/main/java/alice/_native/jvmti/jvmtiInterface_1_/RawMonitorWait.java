package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *RawMonitorWait) (jvmtiEnv* env, jrawMonitorID monitor, jlong millis);
public final class RawMonitorWait {

    private static final long code_base = JVMTINativeCall.create(RawMonitorWait.class, "()I", JVMTINativeCall.RAW_MONITOR_WAIT, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private RawMonitorWait() {
    }

    public synchronized static int invoke(long JVMTIEnv, long monitor, long millis) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, monitor);
        JVMTINativeCall.setArg(code_base, 1, millis);
        return holder();
    }

    public synchronized static int invoke(long monitor, long millis) {
        return invoke(JNIUtil.getJVMTIEnv(), monitor, millis);
    }
}
