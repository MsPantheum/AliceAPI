package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ForceEarlyReturnObject) (jvmtiEnv* env, jthread thread, jobject value);
public final class ForceEarlyReturnObject {

    private ForceEarlyReturnObject() {
    }

    public static int invoke(long thread, long value) {
        return alice._native.jvmti.jvmtiInterface_1_.ForceEarlyReturnObject.invoke(JNIUtil.getJVMTIEnv(), thread, value);
    }
}
