package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ForceEarlyReturnLong) (jvmtiEnv* env, jthread thread, jlong value);
public final class ForceEarlyReturnLong {

    private ForceEarlyReturnLong() {
    }

    public static int invoke(long thread, long value) {
        return alice._native.jvmti.jvmtiInterface_1_.ForceEarlyReturnLong.invoke(JNIUtil.getJVMTIEnv(), thread, value);
    }
}
