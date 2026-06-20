package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *DestroyRawMonitor) (jvmtiEnv* env, jrawMonitorID monitor);
public final class DestroyRawMonitor {

    private static final long code_base = JVMTINativeCall.create(DestroyRawMonitor.class, "()I", JVMTINativeCall.DESTROY_RAW_MONITOR, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private DestroyRawMonitor() {
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
