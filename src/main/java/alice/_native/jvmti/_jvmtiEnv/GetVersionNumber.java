package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetVersionNumber) (jvmtiEnv* env, jint* version_ptr);
public final class GetVersionNumber {

    private GetVersionNumber() {
    }

    public static int invoke(long version_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetVersionNumber.invoke(JNIUtil.getJVMTIEnv(), version_ptr);
    }
}
