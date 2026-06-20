package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetEnvironmentLocalStorage) (jvmtiEnv* env, void** data_ptr);
public final class GetEnvironmentLocalStorage {

    private static final long code_base = JVMTINativeCall.create(GetEnvironmentLocalStorage.class, "()I", JVMTINativeCall.GET_ENVIRONMENT_LOCAL_STORAGE, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetEnvironmentLocalStorage() {
    }

    public synchronized static int invoke(long JVMTIEnv, long data_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, data_ptr);
        return holder();
    }

    public synchronized static int invoke(long data_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), data_ptr);
    }
}
