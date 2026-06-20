package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetThreadGroupChildren) (jvmtiEnv* env, jthreadGroup group, jint* thread_count_ptr, jthread** threads_ptr, jint* group_count_ptr, jthreadGroup** groups_ptr);
public final class GetThreadGroupChildren {

    private GetThreadGroupChildren() {
    }

    public static int invoke(long group, long thread_count_ptr, long threads_ptr, long group_count_ptr, long groups_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetThreadGroupChildren.invoke(JNIUtil.getJVMTIEnv(), group, thread_count_ptr, threads_ptr, group_count_ptr, groups_ptr);
    }
}
