package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetMethodDeclaringClass) (jvmtiEnv* env, jmethodID method, jclass* declaring_class_ptr);
public final class GetMethodDeclaringClass {

    private GetMethodDeclaringClass() {
    }

    public static int invoke(long method, long declaring_class_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetMethodDeclaringClass.invoke(JNIUtil.getJVMTIEnv(), method, declaring_class_ptr);
    }
}
