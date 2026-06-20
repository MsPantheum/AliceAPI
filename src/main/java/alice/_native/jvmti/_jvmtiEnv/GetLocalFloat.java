package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetLocalFloat) (jvmtiEnv* env, jthread thread, jint depth, jint slot, jfloat* value_ptr);
public final class GetLocalFloat {

    private GetLocalFloat() {
    }

    public static int invoke(long thread, int depth, int slot, long value_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetLocalFloat.invoke(JNIUtil.getJVMTIEnv(), thread, depth, slot, value_ptr);
    }
}
