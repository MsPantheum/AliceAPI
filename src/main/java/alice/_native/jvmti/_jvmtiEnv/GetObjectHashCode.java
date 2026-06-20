package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetObjectHashCode) (jvmtiEnv* env, jobject object, jint* hash_code_ptr);
public final class GetObjectHashCode {

    private GetObjectHashCode() {
    }

    public static int invoke(long object, long hash_code_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetObjectHashCode.invoke(JNIUtil.getJVMTIEnv(), object, hash_code_ptr);
    }
}
