package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetMethodModifiers) (jvmtiEnv* env, jmethodID method, jint* modifiers_ptr);
public final class GetMethodModifiers {

    private static final long code_base = JVMTINativeCall.create(GetMethodModifiers.class, "()I", JVMTINativeCall.GET_METHOD_MODIFIERS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetMethodModifiers() {
    }

    public synchronized static int invoke(long JVMTIEnv, long method, long modifiers_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, method);
        JVMTINativeCall.setArg(code_base, 1, modifiers_ptr);
        return holder();
    }

    public synchronized static int invoke(long method, long modifiers_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), method, modifiers_ptr);
    }
}
