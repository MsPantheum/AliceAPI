package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetLocalDouble) (jvmtiEnv* env, jthread thread, jint depth, jint slot, jdouble value);
public final class SetLocalDouble {

    private SetLocalDouble() {
    }

    public static int invoke(long thread, int depth, int slot, double value) {
        return alice._native.jvmti.jvmtiInterface_1_.SetLocalDouble.invoke(JNIUtil.getJVMTIEnv(), thread, depth, slot, value);
    }
}
