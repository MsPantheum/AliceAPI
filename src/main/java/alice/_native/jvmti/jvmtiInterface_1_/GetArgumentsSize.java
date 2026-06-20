package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetArgumentsSize) (jvmtiEnv* env, jmethodID method, jint* size_ptr);
public final class GetArgumentsSize {

    private static final long code_base = JVMTINativeCall.create(GetArgumentsSize.class, "()I", JVMTINativeCall.GET_ARGUMENTS_SIZE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetArgumentsSize() {
    }

    public synchronized static int invoke(long JVMTIEnv, long method, long size_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, method);
        JVMTINativeCall.setArg(code_base, 1, size_ptr);
        return holder();
    }

    public synchronized static int invoke(long method, long size_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), method, size_ptr);
    }
}
