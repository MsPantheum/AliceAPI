package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetTag) (jvmtiEnv* env, jobject object, jlong* tag_ptr);
public final class GetTag {

    private GetTag() {
    }

    public static int invoke(long object, long tag_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetTag.invoke(JNIUtil.getJVMTIEnv(), object, tag_ptr);
    }
}
