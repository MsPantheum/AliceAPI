package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetThreadGroupInfo) (jvmtiEnv* env, jthreadGroup group, jvmtiThreadGroupInfo* info_ptr);
public final class GetThreadGroupInfo {

    private static final long code_base = JVMTINativeCall.create(GetThreadGroupInfo.class, "()I", JVMTINativeCall.GET_THREAD_GROUP_INFO, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetThreadGroupInfo() {
    }

    public synchronized static int invoke(long JVMTIEnv, long group, long info_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, group);
        JVMTINativeCall.setArg(code_base, 1, info_ptr);
        return holder();
    }

    public synchronized static int invoke(long group, long info_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), group, info_ptr);
    }
}
