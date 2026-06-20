package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetLocalDouble) (jvmtiEnv* env, jthread thread, jint depth, jint slot, jdouble value);
public final class SetLocalDouble {

    private static final long code_base = JVMTINativeCall.create(SetLocalDouble.class, "()I", JVMTINativeCall.SET_LOCAL_DOUBLE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.DOUBLE});

    private static native int holder();

    private SetLocalDouble() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, int depth, int slot, double value) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, depth);
        JVMTINativeCall.setArg(code_base, 2, slot);
        JVMTINativeCall.setArg(code_base, 3, value);
        return holder();
    }

    public synchronized static int invoke(long thread, int depth, int slot, double value) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, depth, slot, value);
    }
}
