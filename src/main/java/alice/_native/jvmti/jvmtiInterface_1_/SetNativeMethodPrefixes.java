package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetNativeMethodPrefixes) (jvmtiEnv* env, jint prefix_count, char** prefixes);
public final class SetNativeMethodPrefixes {

    private static final long code_base = JVMTINativeCall.create(SetNativeMethodPrefixes.class, "()I", JVMTINativeCall.SET_NATIVE_METHOD_PREFIXES, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private SetNativeMethodPrefixes() {
    }

    public synchronized static int invoke(long JVMTIEnv, int prefix_count, long prefixes) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, prefix_count);
        JVMTINativeCall.setArg(code_base, 1, prefixes);
        return holder();
    }

    public synchronized static int invoke(int prefix_count, long prefixes) {
        return invoke(JNIUtil.getJVMTIEnv(), prefix_count, prefixes);
    }
}
