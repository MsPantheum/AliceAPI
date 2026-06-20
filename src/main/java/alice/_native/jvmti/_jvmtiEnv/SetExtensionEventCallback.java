package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetExtensionEventCallback) (jvmtiEnv* env, jint extension_event_index, jvmtiExtensionEvent callback);
public final class SetExtensionEventCallback {

    private SetExtensionEventCallback() {
    }

    public static int invoke(int extension_event_index, long callback) {
        return alice._native.jvmti.jvmtiInterface_1_.SetExtensionEventCallback.invoke(JNIUtil.getJVMTIEnv(), extension_event_index, callback);
    }
}
