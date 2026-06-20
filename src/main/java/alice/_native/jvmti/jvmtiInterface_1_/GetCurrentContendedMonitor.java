package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetCurrentContendedMonitor) (jvmtiEnv* env, jthread thread, jobject* monitor_ptr);
public final class GetCurrentContendedMonitor {

    private static final long code_base = JVMTINativeCall.create(GetCurrentContendedMonitor.class, "()I", JVMTINativeCall.GET_CURRENT_CONTENDED_MONITOR, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetCurrentContendedMonitor() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, long monitor_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, monitor_ptr);
        return holder();
    }

    public synchronized static int invoke(long thread, long monitor_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, monitor_ptr);
    }
}
