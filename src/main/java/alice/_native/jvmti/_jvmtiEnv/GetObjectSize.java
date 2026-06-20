package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetObjectSize) (jvmtiEnv* env, jobject object, jlong* size_ptr);
public final class GetObjectSize {

    private GetObjectSize() {
    }

    public static int invoke(long object, long size_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetObjectSize.invoke(JNIUtil.getJVMTIEnv(), object, size_ptr);
    }
}
