package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetBytecodes) (jvmtiEnv* env, jmethodID method, jint* bytecode_count_ptr, unsigned char** bytecodes_ptr);
public final class GetBytecodes {

    private static final long code_base = JVMTINativeCall.create(GetBytecodes.class, "()I", JVMTINativeCall.GET_BYTECODES, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetBytecodes() {
    }

    public synchronized static int invoke(long JVMTIEnv, long method, long bytecode_count_ptr, long bytecodes_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, method);
        JVMTINativeCall.setArg(code_base, 1, bytecode_count_ptr);
        JVMTINativeCall.setArg(code_base, 2, bytecodes_ptr);
        return holder();
    }

    public synchronized static int invoke(long method, long bytecode_count_ptr, long bytecodes_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), method, bytecode_count_ptr, bytecodes_ptr);
    }
}
