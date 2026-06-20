package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetExtensionFunctions) (jvmtiEnv* env, jint* extension_count_ptr, jvmtiExtensionFunctionInfo** extensions);
public final class GetExtensionFunctions {

    private static final long code_base = JVMTINativeCall.create(GetExtensionFunctions.class, "()I", JVMTINativeCall.GET_EXTENSION_FUNCTIONS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetExtensionFunctions() {
    }

    public synchronized static int invoke(long JVMTIEnv, long extension_count_ptr, long extensions) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, extension_count_ptr);
        JVMTINativeCall.setArg(code_base, 1, extensions);
        return holder();
    }

    public synchronized static int invoke(long extension_count_ptr, long extensions) {
        return invoke(JNIUtil.getJVMTIEnv(), extension_count_ptr, extensions);
    }
}
