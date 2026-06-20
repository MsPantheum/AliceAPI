package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetPhase) (jvmtiEnv* env, jvmtiPhase* phase_ptr);
public final class GetPhase {

    private GetPhase() {
    }

    public static int invoke(long phase_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetPhase.invoke(JNIUtil.getJVMTIEnv(), phase_ptr);
    }
}
