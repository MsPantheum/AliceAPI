package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetLocalFloat) (jvmtiEnv* env, jthread thread, jint depth, jint slot, jfloat* value_ptr);
public final class GetLocalFloat {

    private static final long code_base = JVMTINativeCall.create(GetLocalFloat.class, "()I", JVMTINativeCall.GET_LOCAL_FLOAT, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetLocalFloat() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, int depth, int slot, long value_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, depth);
        JVMTINativeCall.setArg(code_base, 2, slot);
        JVMTINativeCall.setArg(code_base, 3, value_ptr);
        return holder();
    }

    public synchronized static int invoke(long thread, int depth, int slot, long value_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, depth, slot, value_ptr);
    }
}
