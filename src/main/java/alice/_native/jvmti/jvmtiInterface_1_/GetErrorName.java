package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetErrorName) (jvmtiEnv* env, jvmtiError error, char** name_ptr);
public final class GetErrorName {

    private static final long code_base = JVMTINativeCall.create(GetErrorName.class, "()I", JVMTINativeCall.GET_ERROR_NAME, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetErrorName() {
    }

    public synchronized static int invoke(long JVMTIEnv, int error, long name_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, error);
        JVMTINativeCall.setArg(code_base, 1, name_ptr);
        return holder();
    }

    public synchronized static int invoke(int error, long name_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), error, name_ptr);
    }
}
