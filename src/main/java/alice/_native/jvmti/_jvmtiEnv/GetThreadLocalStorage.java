package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetThreadLocalStorage) (jvmtiEnv* env, jthread thread, void** data_ptr);
public final class GetThreadLocalStorage {

    private GetThreadLocalStorage() {
    }

    public static int invoke(long thread, long data_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetThreadLocalStorage.invoke(JNIUtil.getJVMTIEnv(), thread, data_ptr);
    }
}
