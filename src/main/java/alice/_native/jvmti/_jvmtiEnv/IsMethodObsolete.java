package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IsMethodObsolete) (jvmtiEnv* env, jmethodID method, jboolean* is_obsolete_ptr);
public final class IsMethodObsolete {

    private IsMethodObsolete() {
    }

    public static int invoke(long method, long is_obsolete_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.IsMethodObsolete.invoke(JNIUtil.getJVMTIEnv(), method, is_obsolete_ptr);
    }
}
