package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetCurrentThreadCpuTime) (jvmtiEnv* env, jlong* nanos_ptr);
public final class GetCurrentThreadCpuTime {

    private GetCurrentThreadCpuTime() {
    }

    public static int invoke(long nanos_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetCurrentThreadCpuTime.invoke(JNIUtil.getJVMTIEnv(), nanos_ptr);
    }
}
