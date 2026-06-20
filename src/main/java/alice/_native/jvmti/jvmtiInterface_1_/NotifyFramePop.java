package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *NotifyFramePop) (jvmtiEnv* env, jthread thread, jint depth);
public final class NotifyFramePop {

    private static final long code_base = JVMTINativeCall.create(NotifyFramePop.class, "()I", JVMTINativeCall.NOTIFY_FRAME_POP, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private NotifyFramePop() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, int depth) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, depth);
        return holder();
    }

    public synchronized static int invoke(long thread, int depth) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, depth);
    }
}
