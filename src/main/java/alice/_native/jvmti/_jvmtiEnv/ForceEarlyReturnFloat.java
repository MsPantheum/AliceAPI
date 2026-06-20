package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ForceEarlyReturnFloat) (jvmtiEnv* env, jthread thread, jfloat value);
public final class ForceEarlyReturnFloat {

    private ForceEarlyReturnFloat() {
    }

    public static int invoke(long thread, float value) {
        return alice._native.jvmti.jvmtiInterface_1_.ForceEarlyReturnFloat.invoke(JNIUtil.getJVMTIEnv(), thread, value);
    }
}
