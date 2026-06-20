package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ForceEarlyReturnDouble) (jvmtiEnv* env, jthread thread, jdouble value);
public final class ForceEarlyReturnDouble {

    private ForceEarlyReturnDouble() {
    }

    public static int invoke(long thread, double value) {
        return alice._native.jvmti.jvmtiInterface_1_.ForceEarlyReturnDouble.invoke(JNIUtil.getJVMTIEnv(), thread, value);
    }
}
