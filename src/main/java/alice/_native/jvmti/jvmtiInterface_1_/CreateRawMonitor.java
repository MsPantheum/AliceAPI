package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *CreateRawMonitor) (jvmtiEnv* env, const char* name, jrawMonitorID* monitor_ptr);
public final class CreateRawMonitor {

    private static final long code_base = JVMTINativeCall.create(CreateRawMonitor.class, "()I", JVMTINativeCall.CREATE_RAW_MONITOR, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private CreateRawMonitor() {
    }

    public synchronized static int invoke(long JVMTIEnv, long name, long monitor_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, name);
        JVMTINativeCall.setArg(code_base, 1, monitor_ptr);
        return holder();
    }

    public synchronized static int invoke(long name, long monitor_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), name, monitor_ptr);
    }
}
