package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *RawMonitorExit) (jvmtiEnv* env, jrawMonitorID monitor);
public final class RawMonitorExit {

    private static final long code_base = JVMTINativeCall.create(RawMonitorExit.class, "()I", JVMTINativeCall.RAW_MONITOR_EXIT, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private RawMonitorExit() {
    }

    public synchronized static int invoke(long JVMTIEnv, long monitor) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, monitor);
        return holder();
    }

    public synchronized static int invoke(long monitor) {
        return invoke(JNIUtil.getJVMTIEnv(), monitor);
    }
}
