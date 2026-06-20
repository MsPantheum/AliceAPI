package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetTopThreadGroups) (jvmtiEnv* env, jint* group_count_ptr, jthreadGroup** groups_ptr);
public final class GetTopThreadGroups {

    private static final long code_base = JVMTINativeCall.create(GetTopThreadGroups.class, "()I", JVMTINativeCall.GET_TOP_THREAD_GROUPS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetTopThreadGroups() {
    }

    public synchronized static int invoke(long JVMTIEnv, long group_count_ptr, long groups_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, group_count_ptr);
        JVMTINativeCall.setArg(code_base, 1, groups_ptr);
        return holder();
    }

    public synchronized static int invoke(long group_count_ptr, long groups_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), group_count_ptr, groups_ptr);
    }
}
