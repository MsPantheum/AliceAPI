package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetClassMethods) (jvmtiEnv* env, jclass klass, jint* method_count_ptr, jmethodID** methods_ptr);
public final class GetClassMethods {

    private static final long code_base = JVMTINativeCall.create(GetClassMethods.class, "()I", JVMTINativeCall.GET_CLASS_METHODS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetClassMethods() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long method_count_ptr, long methods_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, method_count_ptr);
        JVMTINativeCall.setArg(code_base, 2, methods_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long method_count_ptr, long methods_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, method_count_ptr, methods_ptr);
    }
}
