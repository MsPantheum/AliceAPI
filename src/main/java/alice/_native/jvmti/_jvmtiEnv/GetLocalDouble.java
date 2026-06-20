package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetLocalDouble) (jvmtiEnv* env, jthread thread, jint depth, jint slot, jdouble* value_ptr);
public final class GetLocalDouble {

    private GetLocalDouble() {
    }

    public static int invoke(long thread, int depth, int slot, long value_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetLocalDouble.invoke(JNIUtil.getJVMTIEnv(), thread, depth, slot, value_ptr);
    }
}
