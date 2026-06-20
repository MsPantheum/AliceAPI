package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *PopFrame) (jvmtiEnv* env, jthread thread);
public final class PopFrame {

    private static final long code_base = JVMTINativeCall.create(PopFrame.class, "()I", JVMTINativeCall.POP_FRAME, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private PopFrame() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        return holder();
    }

    public synchronized static int invoke(long thread) {
        return invoke(JNIUtil.getJVMTIEnv(), thread);
    }
}
