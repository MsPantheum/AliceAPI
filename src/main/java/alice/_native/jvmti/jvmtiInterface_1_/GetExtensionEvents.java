package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetExtensionEvents) (jvmtiEnv* env, jint* extension_count_ptr, jvmtiExtensionEventInfo** extensions);
public final class GetExtensionEvents {

    private static final long code_base = JVMTINativeCall.create(GetExtensionEvents.class, "()I", JVMTINativeCall.GET_EXTENSION_EVENTS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetExtensionEvents() {
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
