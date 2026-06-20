package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetLocalObject) (jvmtiEnv* env, jthread thread, jint depth, jint slot, jobject* value_ptr);
public final class GetLocalObject {

    private GetLocalObject() {
    }

    public static int invoke(long thread, int depth, int slot, long value_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetLocalObject.invoke(JNIUtil.getJVMTIEnv(), thread, depth, slot, value_ptr);
    }
}
