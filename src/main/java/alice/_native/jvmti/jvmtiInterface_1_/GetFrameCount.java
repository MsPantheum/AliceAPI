package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetFrameCount) (jvmtiEnv* env, jthread thread, jint* count_ptr);
public final class GetFrameCount {

    private static final long code_base = JVMTINativeCall.create(GetFrameCount.class, "()I", JVMTINativeCall.GET_FRAME_COUNT, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetFrameCount() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, long count_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, count_ptr);
        return holder();
    }

    public synchronized static int invoke(long thread, long count_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, count_ptr);
    }
}
