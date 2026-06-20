package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetMethodModifiers) (jvmtiEnv* env, jmethodID method, jint* modifiers_ptr);
public final class GetMethodModifiers {

    private GetMethodModifiers() {
    }

    public static int invoke(long method, long modifiers_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetMethodModifiers.invoke(JNIUtil.getJVMTIEnv(), method, modifiers_ptr);
    }
}
