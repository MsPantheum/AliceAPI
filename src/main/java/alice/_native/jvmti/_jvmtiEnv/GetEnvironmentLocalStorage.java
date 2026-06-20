package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetEnvironmentLocalStorage) (jvmtiEnv* env, void** data_ptr);
public final class GetEnvironmentLocalStorage {

    private GetEnvironmentLocalStorage() {
    }

    public static int invoke(long data_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetEnvironmentLocalStorage.invoke(JNIUtil.getJVMTIEnv(), data_ptr);
    }
}
