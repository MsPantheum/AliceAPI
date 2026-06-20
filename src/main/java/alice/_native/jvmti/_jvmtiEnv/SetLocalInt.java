package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetLocalInt) (jvmtiEnv* env, jthread thread, jint depth, jint slot, jint value);
public final class SetLocalInt {

    private SetLocalInt() {
    }

    public static int invoke(long thread, int depth, int slot, int value) {
        return alice._native.jvmti.jvmtiInterface_1_.SetLocalInt.invoke(JNIUtil.getJVMTIEnv(), thread, depth, slot, value);
    }
}
