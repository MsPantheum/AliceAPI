package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetBytecodes) (jvmtiEnv* env, jmethodID method, jint* bytecode_count_ptr, unsigned char** bytecodes_ptr);
public final class GetBytecodes {

    private GetBytecodes() {
    }

    public static int invoke(long method, long bytecode_count_ptr, long bytecodes_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetBytecodes.invoke(JNIUtil.getJVMTIEnv(), method, bytecode_count_ptr, bytecodes_ptr);
    }
}
