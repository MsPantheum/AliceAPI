package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ForceEarlyReturnInt) (jvmtiEnv* env, jthread thread, jint value);
public final class ForceEarlyReturnInt {

    private ForceEarlyReturnInt() {
    }

    public static int invoke(long thread, int value) {
        return alice._native.jvmti.jvmtiInterface_1_.ForceEarlyReturnInt.invoke(JNIUtil.getJVMTIEnv(), thread, value);
    }
}
