package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetEnvironmentLocalStorage) (jvmtiEnv* env, const void* data);
public final class SetEnvironmentLocalStorage {

    private SetEnvironmentLocalStorage() {
    }

    public static int invoke(long data) {
        return alice._native.jvmti.jvmtiInterface_1_.SetEnvironmentLocalStorage.invoke(JNIUtil.getJVMTIEnv(), data);
    }
}
