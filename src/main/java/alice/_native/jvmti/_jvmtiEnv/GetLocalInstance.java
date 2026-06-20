package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetLocalInstance) (jvmtiEnv* env, jthread thread, jint depth, jobject* value_ptr);
public final class GetLocalInstance {

    private GetLocalInstance() {
    }

    public static int invoke(long thread, int depth, long value_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetLocalInstance.invoke(JNIUtil.getJVMTIEnv(), thread, depth, value_ptr);
    }
}
