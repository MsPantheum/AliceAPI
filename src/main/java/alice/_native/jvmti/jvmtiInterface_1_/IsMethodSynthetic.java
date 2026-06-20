package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IsMethodSynthetic) (jvmtiEnv* env, jmethodID method, jboolean* is_synthetic_ptr);
public final class IsMethodSynthetic {

    private static final long code_base = JVMTINativeCall.create(IsMethodSynthetic.class, "()I", JVMTINativeCall.IS_METHOD_SYNTHETIC, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private IsMethodSynthetic() {
    }

    public synchronized static int invoke(long JVMTIEnv, long method, long is_synthetic_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, method);
        JVMTINativeCall.setArg(code_base, 1, is_synthetic_ptr);
        return holder();
    }

    public synchronized static int invoke(long method, long is_synthetic_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), method, is_synthetic_ptr);
    }
}
