package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetTime) (jvmtiEnv* env, jlong* nanos_ptr);
public final class GetTime {

    private GetTime() {
    }

    public static int invoke(long nanos_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetTime.invoke(JNIUtil.getJVMTIEnv(), nanos_ptr);
    }
}
