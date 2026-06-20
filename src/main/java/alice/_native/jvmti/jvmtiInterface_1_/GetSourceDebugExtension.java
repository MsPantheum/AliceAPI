package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetSourceDebugExtension) (jvmtiEnv* env, jclass klass, char** source_debug_extension_ptr);
public final class GetSourceDebugExtension {

    private static final long code_base = JVMTINativeCall.create(GetSourceDebugExtension.class, "()I", JVMTINativeCall.GET_SOURCE_DEBUG_EXTENSION, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetSourceDebugExtension() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long source_debug_extension_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, source_debug_extension_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long source_debug_extension_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, source_debug_extension_ptr);
    }
}
