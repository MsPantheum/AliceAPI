package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetLocalInt) (jvmtiEnv* env, jthread thread, jint depth, jint slot, jint* value_ptr);
public final class GetLocalInt {

    private GetLocalInt() {
    }

    public static int invoke(long thread, int depth, int slot, long value_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetLocalInt.invoke(JNIUtil.getJVMTIEnv(), thread, depth, slot, value_ptr);
    }
}
