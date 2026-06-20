package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetMethodName) (jvmtiEnv* env, jmethodID method, char** name_ptr, char** signature_ptr, char** generic_ptr);
public final class GetMethodName {

    private GetMethodName() {
    }

    public static int invoke(long method, long name_ptr, long signature_ptr, long generic_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetMethodName.invoke(JNIUtil.getJVMTIEnv(), method, name_ptr, signature_ptr, generic_ptr);
    }
}
