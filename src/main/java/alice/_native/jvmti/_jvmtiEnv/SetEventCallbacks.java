package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetEventCallbacks) (jvmtiEnv* env, const jvmtiEventCallbacks* callbacks, jint size_of_callbacks);
public final class SetEventCallbacks {

    private SetEventCallbacks() {
    }

    public static int invoke(long callbacks, int size_of_callbacks) {
        return alice._native.jvmti.jvmtiInterface_1_.SetEventCallbacks.invoke(JNIUtil.getJVMTIEnv(), callbacks, size_of_callbacks);
    }
}
