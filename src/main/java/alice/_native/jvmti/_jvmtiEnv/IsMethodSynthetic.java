package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IsMethodSynthetic) (jvmtiEnv* env, jmethodID method, jboolean* is_synthetic_ptr);
public final class IsMethodSynthetic {

    private IsMethodSynthetic() {
    }

    public static int invoke(long method, long is_synthetic_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.IsMethodSynthetic.invoke(JNIUtil.getJVMTIEnv(), method, is_synthetic_ptr);
    }
}
