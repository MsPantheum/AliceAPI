package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetErrorName) (jvmtiEnv* env, jvmtiError error, char** name_ptr);
public final class GetErrorName {

    private GetErrorName() {
    }

    public static int invoke(int error, long name_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetErrorName.invoke(JNIUtil.getJVMTIEnv(), error, name_ptr);
    }
}
