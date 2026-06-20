package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetThreadLocalStorage) (jvmtiEnv* env, jthread thread, const void* data);
public final class SetThreadLocalStorage {

    private static final long code_base = JVMTINativeCall.create(SetThreadLocalStorage.class, "()I", JVMTINativeCall.SET_THREAD_LOCAL_STORAGE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private SetThreadLocalStorage() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, long data) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, data);
        return holder();
    }

    public synchronized static int invoke(long thread, long data) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, data);
    }
}
