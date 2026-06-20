package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetFrameCount) (jvmtiEnv* env, jthread thread, jint* count_ptr);
public final class GetFrameCount {

    private GetFrameCount() {
    }

    public static int invoke(long thread, long count_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetFrameCount.invoke(JNIUtil.getJVMTIEnv(), thread, count_ptr);
    }
}
