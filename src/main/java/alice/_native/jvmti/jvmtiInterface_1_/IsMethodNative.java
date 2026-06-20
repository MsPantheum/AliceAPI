package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IsMethodNative) (jvmtiEnv* env, jmethodID method, jboolean* is_native_ptr);
public final class IsMethodNative {

    private static final long code_base = JVMTINativeCall.create(IsMethodNative.class, "()I", JVMTINativeCall.IS_METHOD_NATIVE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private IsMethodNative() {
    }

    public synchronized static int invoke(long JVMTIEnv, long method, long is_native_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, method);
        JVMTINativeCall.setArg(code_base, 1, is_native_ptr);
        return holder();
    }

    public synchronized static int invoke(long method, long is_native_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), method, is_native_ptr);
    }
}
