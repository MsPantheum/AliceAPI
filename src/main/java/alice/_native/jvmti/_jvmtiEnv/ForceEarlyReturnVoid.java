package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ForceEarlyReturnVoid) (jvmtiEnv* env, jthread thread);
public final class ForceEarlyReturnVoid {

    private ForceEarlyReturnVoid() {
    }

    public static int invoke(long thread) {
        return alice._native.jvmti.jvmtiInterface_1_.ForceEarlyReturnVoid.invoke(JNIUtil.getJVMTIEnv(), thread);
    }
}
