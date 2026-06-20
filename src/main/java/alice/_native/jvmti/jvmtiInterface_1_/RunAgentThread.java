package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *RunAgentThread) (jvmtiEnv* env, jthread thread, jvmtiStartFunction proc, const void* arg, jint priority);
public final class RunAgentThread {

    private static final long code_base = JVMTINativeCall.create(RunAgentThread.class, "()I", JVMTINativeCall.RUN_AGENT_THREAD, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private RunAgentThread() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, long proc, long arg, int priority) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, proc);
        JVMTINativeCall.setArg(code_base, 2, arg);
        JVMTINativeCall.setArg(code_base, 3, priority);
        return holder();
    }

    public synchronized static int invoke(long thread, long proc, long arg, int priority) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, proc, arg, priority);
    }
}
