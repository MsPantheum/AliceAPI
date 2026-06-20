package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetThreadCpuTime) (jvmtiEnv* env, jthread thread, jlong* nanos_ptr);
public final class GetThreadCpuTime {

    private static final long code_base = JVMTINativeCall.create(GetThreadCpuTime.class, "()I", JVMTINativeCall.GET_THREAD_CPU_TIME, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetThreadCpuTime() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, long nanos_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, nanos_ptr);
        return holder();
    }

    public synchronized static int invoke(long thread, long nanos_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, nanos_ptr);
    }
}
