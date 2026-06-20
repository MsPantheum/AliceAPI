package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetTag) (jvmtiEnv* env, jobject object, jlong tag);
public final class SetTag {

    private SetTag() {
    }

    public static int invoke(long object, long tag) {
        return alice._native.jvmti.jvmtiInterface_1_.SetTag.invoke(JNIUtil.getJVMTIEnv(), object, tag);
    }
}
