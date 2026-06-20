package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *RawMonitorEnter) (jvmtiEnv* env, jrawMonitorID monitor);
public final class RawMonitorEnter {

    private static final long code_base = JVMTINativeCall.create(RawMonitorEnter.class, "()I", JVMTINativeCall.RAW_MONITOR_ENTER, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private RawMonitorEnter() {
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
