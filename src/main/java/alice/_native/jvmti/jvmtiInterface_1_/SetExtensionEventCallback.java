package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetExtensionEventCallback) (jvmtiEnv* env, jint extension_event_index, jvmtiExtensionEvent callback);
public final class SetExtensionEventCallback {

    private static final long code_base = JVMTINativeCall.create(SetExtensionEventCallback.class, "()I", JVMTINativeCall.SET_EXTENSION_EVENT_CALLBACK, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private SetExtensionEventCallback() {
    }

    public synchronized static int invoke(long JVMTIEnv, int extension_event_index, long callback) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, extension_event_index);
        JVMTINativeCall.setArg(code_base, 1, callback);
        return holder();
    }

    public synchronized static int invoke(int extension_event_index, long callback) {
        return invoke(JNIUtil.getJVMTIEnv(), extension_event_index, callback);
    }
}
