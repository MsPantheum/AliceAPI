package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetExtensionEvents) (jvmtiEnv* env, jint* extension_count_ptr, jvmtiExtensionEventInfo** extensions);
public final class GetExtensionEvents {

    private GetExtensionEvents() {
    }

    public static int invoke(long extension_count_ptr, long extensions) {
        return alice._native.jvmti.jvmtiInterface_1_.GetExtensionEvents.invoke(JNIUtil.getJVMTIEnv(), extension_count_ptr, extensions);
    }
}
