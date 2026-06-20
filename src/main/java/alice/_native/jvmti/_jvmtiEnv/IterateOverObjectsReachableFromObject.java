package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IterateOverObjectsReachableFromObject) (jvmtiEnv* env, jobject object, jvmtiObjectReferenceCallback object_reference_callback, const void* user_data);
public final class IterateOverObjectsReachableFromObject {

    private IterateOverObjectsReachableFromObject() {
    }

    public static int invoke(long object, long object_reference_callback, long user_data) {
        return alice._native.jvmti.jvmtiInterface_1_.IterateOverObjectsReachableFromObject.invoke(JNIUtil.getJVMTIEnv(), object, object_reference_callback, user_data);
    }
}
