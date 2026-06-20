package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *StopThread) (jvmtiEnv* env, jthread thread, jobject exception);
public final class StopThread {

    private static final long code_base = JVMTINativeCall.create(StopThread.class, "()I", JVMTINativeCall.STOP_THREAD, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private StopThread() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, long exception) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, exception);
        return holder();
    }

    public synchronized static int invoke(long thread, long exception) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, exception);
    }
}
