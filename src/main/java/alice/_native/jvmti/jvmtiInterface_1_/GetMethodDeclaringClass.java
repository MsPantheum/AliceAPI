package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetMethodDeclaringClass) (jvmtiEnv* env, jmethodID method, jclass* declaring_class_ptr);
public final class GetMethodDeclaringClass {

    private static final long code_base = JVMTINativeCall.create(GetMethodDeclaringClass.class, "()I", JVMTINativeCall.GET_METHOD_DECLARING_CLASS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetMethodDeclaringClass() {
    }

    public synchronized static int invoke(long JVMTIEnv, long method, long declaring_class_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, method);
        JVMTINativeCall.setArg(code_base, 1, declaring_class_ptr);
        return holder();
    }

    public synchronized static int invoke(long method, long declaring_class_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), method, declaring_class_ptr);
    }
}
