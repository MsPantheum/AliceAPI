package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetLocalObject) (jvmtiEnv* env, jthread thread, jint depth, jint slot, jobject value);
public final class SetLocalObject {

    private SetLocalObject() {
    }

    public static int invoke(long thread, int depth, int slot, long value) {
        return alice._native.jvmti.jvmtiInterface_1_.SetLocalObject.invoke(JNIUtil.getJVMTIEnv(), thread, depth, slot, value);
    }
}
