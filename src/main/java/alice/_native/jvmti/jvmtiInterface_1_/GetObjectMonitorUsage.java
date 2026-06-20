package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetObjectMonitorUsage) (jvmtiEnv* env, jobject object, jvmtiMonitorUsage* info_ptr);
public final class GetObjectMonitorUsage {

    private static final long code_base = JVMTINativeCall.create(GetObjectMonitorUsage.class, "()I", JVMTINativeCall.GET_OBJECT_MONITOR_USAGE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetObjectMonitorUsage() {
    }

    public synchronized static int invoke(long JVMTIEnv, long object, long info_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, object);
        JVMTINativeCall.setArg(code_base, 1, info_ptr);
        return holder();
    }

    public synchronized static int invoke(long object, long info_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), object, info_ptr);
    }
}
