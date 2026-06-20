package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetLocalInstance) (jvmtiEnv* env, jthread thread, jint depth, jobject* value_ptr);
public final class GetLocalInstance {

    private static final long code_base = JVMTINativeCall.create(GetLocalInstance.class, "()I", JVMTINativeCall.GET_LOCAL_INSTANCE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetLocalInstance() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, int depth, long value_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, depth);
        JVMTINativeCall.setArg(code_base, 2, value_ptr);
        return holder();
    }

    public synchronized static int invoke(long thread, int depth, long value_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, depth, value_ptr);
    }
}
