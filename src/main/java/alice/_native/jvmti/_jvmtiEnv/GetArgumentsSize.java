package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetArgumentsSize) (jvmtiEnv* env, jmethodID method, jint* size_ptr);
public final class GetArgumentsSize {

    private GetArgumentsSize() {
    }

    public static int invoke(long method, long size_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetArgumentsSize.invoke(JNIUtil.getJVMTIEnv(), method, size_ptr);
    }
}
