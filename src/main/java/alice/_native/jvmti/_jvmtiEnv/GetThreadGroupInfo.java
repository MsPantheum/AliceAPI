package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetThreadGroupInfo) (jvmtiEnv* env, jthreadGroup group, jvmtiThreadGroupInfo* info_ptr);
public final class GetThreadGroupInfo {

    private GetThreadGroupInfo() {
    }

    public static int invoke(long group, long info_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetThreadGroupInfo.invoke(JNIUtil.getJVMTIEnv(), group, info_ptr);
    }
}
