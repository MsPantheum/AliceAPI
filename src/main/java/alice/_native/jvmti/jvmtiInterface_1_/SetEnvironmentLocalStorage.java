package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetEnvironmentLocalStorage) (jvmtiEnv* env, const void* data);
public final class SetEnvironmentLocalStorage {

    private static final long code_base = JVMTINativeCall.create(SetEnvironmentLocalStorage.class, "()I", JVMTINativeCall.SET_ENVIRONMENT_LOCAL_STORAGE, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private SetEnvironmentLocalStorage() {
    }

    public synchronized static int invoke(long JVMTIEnv, long data) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, data);
        return holder();
    }

    public synchronized static int invoke(long data) {
        return invoke(JNIUtil.getJVMTIEnv(), data);
    }
}
