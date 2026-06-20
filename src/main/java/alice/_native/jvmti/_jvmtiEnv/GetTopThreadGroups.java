package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetTopThreadGroups) (jvmtiEnv* env, jint* group_count_ptr, jthreadGroup** groups_ptr);
public final class GetTopThreadGroups {

    private GetTopThreadGroups() {
    }

    public static int invoke(long group_count_ptr, long groups_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetTopThreadGroups.invoke(JNIUtil.getJVMTIEnv(), group_count_ptr, groups_ptr);
    }
}
