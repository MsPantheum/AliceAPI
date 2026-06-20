package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetThreadCpuTime) (jvmtiEnv* env, jthread thread, jlong* nanos_ptr);
public final class GetThreadCpuTime {

    private GetThreadCpuTime() {
    }

    public static int invoke(long thread, long nanos_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetThreadCpuTime.invoke(JNIUtil.getJVMTIEnv(), thread, nanos_ptr);
    }
}
