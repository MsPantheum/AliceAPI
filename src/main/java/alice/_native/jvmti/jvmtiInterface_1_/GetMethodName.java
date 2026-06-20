package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetMethodName) (jvmtiEnv* env, jmethodID method, char** name_ptr, char** signature_ptr, char** generic_ptr);
public final class GetMethodName {

    private static final long code_base = JVMTINativeCall.create(GetMethodName.class, "()I", JVMTINativeCall.GET_METHOD_NAME, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetMethodName() {
    }

    public synchronized static int invoke(long JVMTIEnv, long method, long name_ptr, long signature_ptr, long generic_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, method);
        JVMTINativeCall.setArg(code_base, 1, name_ptr);
        JVMTINativeCall.setArg(code_base, 2, signature_ptr);
        JVMTINativeCall.setArg(code_base, 3, generic_ptr);
        return holder();
    }

    public synchronized static int invoke(long method, long name_ptr, long signature_ptr, long generic_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), method, name_ptr, signature_ptr, generic_ptr);
    }
}
