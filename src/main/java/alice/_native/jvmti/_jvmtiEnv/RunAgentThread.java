package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *RunAgentThread) (jvmtiEnv* env, jthread thread, jvmtiStartFunction proc, const void* arg, jint priority);
public final class RunAgentThread {

    private RunAgentThread() {
    }

    public static int invoke(long thread, long proc, long arg, int priority) {
        return alice._native.jvmti.jvmtiInterface_1_.RunAgentThread.invoke(JNIUtil.getJVMTIEnv(), thread, proc, arg, priority);
    }
}
