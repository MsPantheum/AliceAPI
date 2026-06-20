package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetExtensionFunctions) (jvmtiEnv* env, jint* extension_count_ptr, jvmtiExtensionFunctionInfo** extensions);
public final class GetExtensionFunctions {

    private GetExtensionFunctions() {
    }

    public static int invoke(long extension_count_ptr, long extensions) {
        return alice._native.jvmti.jvmtiInterface_1_.GetExtensionFunctions.invoke(JNIUtil.getJVMTIEnv(), extension_count_ptr, extensions);
    }
}
