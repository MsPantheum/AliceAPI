package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetVerboseFlag) (jvmtiEnv* env, jvmtiVerboseFlag flag, jboolean value);
public final class SetVerboseFlag {

    private static final long code_base = JVMTINativeCall.create(SetVerboseFlag.class, "()I", JVMTINativeCall.SET_VERBOSE_FLAG, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private SetVerboseFlag() {
    }

    public synchronized static int invoke(long JVMTIEnv, int flag, boolean value) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, flag);
        JVMTINativeCall.setArg(code_base, 1, value);
        return holder();
    }

    public synchronized static int invoke(int flag, boolean value) {
        return invoke(JNIUtil.getJVMTIEnv(), flag, value);
    }
}
