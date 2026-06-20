package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetLocalLong) (jvmtiEnv* env, jthread thread, jint depth, jint slot, jlong* value_ptr);
public final class GetLocalLong {

    private GetLocalLong() {
    }

    public static int invoke(long thread, int depth, int slot, long value_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetLocalLong.invoke(JNIUtil.getJVMTIEnv(), thread, depth, slot, value_ptr);
    }
}
