package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetLocalFloat) (jvmtiEnv* env, jthread thread, jint depth, jint slot, jfloat value);
public final class SetLocalFloat {

    private SetLocalFloat() {
    }

    public static int invoke(long thread, int depth, int slot, float value) {
        return alice._native.jvmti.jvmtiInterface_1_.SetLocalFloat.invoke(JNIUtil.getJVMTIEnv(), thread, depth, slot, value);
    }
}
