package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetThreadLocalStorage) (jvmtiEnv* env, jthread thread, void** data_ptr);
public final class GetThreadLocalStorage {

    private static final long code_base = JVMTINativeCall.create(GetThreadLocalStorage.class, "()I", JVMTINativeCall.GET_THREAD_LOCAL_STORAGE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetThreadLocalStorage() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, long data_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, data_ptr);
        return holder();
    }

    public synchronized static int invoke(long thread, long data_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, data_ptr);
    }
}
