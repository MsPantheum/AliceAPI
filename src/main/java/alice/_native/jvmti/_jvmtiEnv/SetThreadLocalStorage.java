package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetThreadLocalStorage) (jvmtiEnv* env, jthread thread, const void* data);
public final class SetThreadLocalStorage {

    private SetThreadLocalStorage() {
    }

    public static int invoke(long thread, long data) {
        return alice._native.jvmti.jvmtiInterface_1_.SetThreadLocalStorage.invoke(JNIUtil.getJVMTIEnv(), thread, data);
    }
}
