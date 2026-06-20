package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetOwnedMonitorInfo) (jvmtiEnv* env, jthread thread, jint* owned_monitor_count_ptr, jobject** owned_monitors_ptr);
public final class GetOwnedMonitorInfo {

    private static final long code_base = JVMTINativeCall.create(GetOwnedMonitorInfo.class, "()I", JVMTINativeCall.GET_OWNED_MONITOR_INFO, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetOwnedMonitorInfo() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, long owned_monitor_count_ptr, long owned_monitors_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, owned_monitor_count_ptr);
        JVMTINativeCall.setArg(code_base, 2, owned_monitors_ptr);
        return holder();
    }

    public synchronized static int invoke(long thread, long owned_monitor_count_ptr, long owned_monitors_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, owned_monitor_count_ptr, owned_monitors_ptr);
    }
}
