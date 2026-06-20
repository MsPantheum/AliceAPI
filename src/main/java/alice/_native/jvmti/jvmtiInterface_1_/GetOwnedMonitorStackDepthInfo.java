package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetOwnedMonitorStackDepthInfo) (jvmtiEnv* env, jthread thread, jint* monitor_info_count_ptr, jvmtiMonitorStackDepthInfo** monitor_info_ptr);
public final class GetOwnedMonitorStackDepthInfo {

    private static final long code_base = JVMTINativeCall.create(GetOwnedMonitorStackDepthInfo.class, "()I", JVMTINativeCall.GET_OWNED_MONITOR_STACK_DEPTH_INFO, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetOwnedMonitorStackDepthInfo() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, long monitor_info_count_ptr, long monitor_info_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, monitor_info_count_ptr);
        JVMTINativeCall.setArg(code_base, 2, monitor_info_ptr);
        return holder();
    }

    public synchronized static int invoke(long thread, long monitor_info_count_ptr, long monitor_info_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, monitor_info_count_ptr, monitor_info_ptr);
    }
}
