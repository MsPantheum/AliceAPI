package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetSourceDebugExtension) (jvmtiEnv* env, jclass klass, char** source_debug_extension_ptr);
public final class GetSourceDebugExtension {

    private GetSourceDebugExtension() {
    }

    public static int invoke(long klass, long source_debug_extension_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetSourceDebugExtension.invoke(JNIUtil.getJVMTIEnv(), klass, source_debug_extension_ptr);
    }
}
