package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetMaxLocals) (jvmtiEnv* env, jmethodID method, jint* max_ptr);
public final class GetMaxLocals {

    private static final long code_base = JVMTINativeCall.create(GetMaxLocals.class, "()I", JVMTINativeCall.GET_MAX_LOCALS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetMaxLocals() {
    }

    public synchronized static int invoke(long JVMTIEnv, long method, long max_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, method);
        JVMTINativeCall.setArg(code_base, 1, max_ptr);
        return holder();
    }

    public synchronized static int invoke(long method, long max_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), method, max_ptr);
    }
}
