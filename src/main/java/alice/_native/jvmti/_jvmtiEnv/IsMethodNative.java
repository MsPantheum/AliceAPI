package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IsMethodNative) (jvmtiEnv* env, jmethodID method, jboolean* is_native_ptr);
public final class IsMethodNative {

    private IsMethodNative() {
    }

    public static int invoke(long method, long is_native_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.IsMethodNative.invoke(JNIUtil.getJVMTIEnv(), method, is_native_ptr);
    }
}
