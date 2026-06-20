package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetThreadGroupChildren) (jvmtiEnv* env, jthreadGroup group, jint* thread_count_ptr, jthread** threads_ptr, jint* group_count_ptr, jthreadGroup** groups_ptr);
public final class GetThreadGroupChildren {

    private static final long code_base = JVMTINativeCall.create(GetThreadGroupChildren.class, "()I", JVMTINativeCall.GET_THREAD_GROUP_CHILDREN, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetThreadGroupChildren() {
    }

    public synchronized static int invoke(long JVMTIEnv, long group, long thread_count_ptr, long threads_ptr, long group_count_ptr, long groups_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, group);
        JVMTINativeCall.setArg(code_base, 1, thread_count_ptr);
        JVMTINativeCall.setArg(code_base, 2, threads_ptr);
        JVMTINativeCall.setArg(code_base, 3, group_count_ptr);
        JVMTINativeCall.setArg(code_base, 4, groups_ptr);
        return holder();
    }

    public synchronized static int invoke(long group, long thread_count_ptr, long threads_ptr, long group_count_ptr, long groups_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), group, thread_count_ptr, threads_ptr, group_count_ptr, groups_ptr);
    }
}
