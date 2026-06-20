package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetLocalLong) (jvmtiEnv* env, jthread thread, jint depth, jint slot, jlong value);
public final class SetLocalLong {

    private SetLocalLong() {
    }

    public static int invoke(long thread, int depth, int slot, long value) {
        return alice._native.jvmti.jvmtiInterface_1_.SetLocalLong.invoke(JNIUtil.getJVMTIEnv(), thread, depth, slot, value);
    }
}
