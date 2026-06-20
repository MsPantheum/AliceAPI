package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetNativeMethodPrefix) (jvmtiEnv* env, const char* prefix);
public final class SetNativeMethodPrefix {

    private static final long code_base = JVMTINativeCall.create(SetNativeMethodPrefix.class, "()I", JVMTINativeCall.SET_NATIVE_METHOD_PREFIX, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private SetNativeMethodPrefix() {
    }

    public synchronized static int invoke(long JVMTIEnv, long prefix) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, prefix);
        return holder();
    }

    public synchronized static int invoke(long prefix) {
        return invoke(JNIUtil.getJVMTIEnv(), prefix);
    }
}
