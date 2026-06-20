package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetLocalFloat) (jvmtiEnv* env, jthread thread, jint depth, jint slot, jfloat value);
public final class SetLocalFloat {

    private static final long code_base = JVMTINativeCall.create(SetLocalFloat.class, "()I", JVMTINativeCall.SET_LOCAL_FLOAT, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.FLOAT});

    private static native int holder();

    private SetLocalFloat() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, int depth, int slot, float value) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, depth);
        JVMTINativeCall.setArg(code_base, 2, slot);
        JVMTINativeCall.setArg(code_base, 3, value);
        return holder();
    }

    public synchronized static int invoke(long thread, int depth, int slot, float value) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, depth, slot, value);
    }
}
