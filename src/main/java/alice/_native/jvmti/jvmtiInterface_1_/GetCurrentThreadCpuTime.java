package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetCurrentThreadCpuTime) (jvmtiEnv* env, jlong* nanos_ptr);
public final class GetCurrentThreadCpuTime {

    private static final long code_base = JVMTINativeCall.create(GetCurrentThreadCpuTime.class, "()I", JVMTINativeCall.GET_CURRENT_THREAD_CPU_TIME, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetCurrentThreadCpuTime() {
    }

    public synchronized static int invoke(long JVMTIEnv, long nanos_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, nanos_ptr);
        return holder();
    }

    public synchronized static int invoke(long nanos_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), nanos_ptr);
    }
}
