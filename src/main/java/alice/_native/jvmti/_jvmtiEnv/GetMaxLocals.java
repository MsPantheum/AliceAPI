package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetMaxLocals) (jvmtiEnv* env, jmethodID method, jint* max_ptr);
public final class GetMaxLocals {

    private GetMaxLocals() {
    }

    public static int invoke(long method, long max_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetMaxLocals.invoke(JNIUtil.getJVMTIEnv(), method, max_ptr);
    }
}
