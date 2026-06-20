package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IterateOverReachableObjects) (jvmtiEnv* env, jvmtiHeapRootCallback heap_root_callback, jvmtiStackReferenceCallback stack_ref_callback, jvmtiObjectReferenceCallback object_ref_callback, const void* user_data);
public final class IterateOverReachableObjects {

    private IterateOverReachableObjects() {
    }

    public static int invoke(long heap_root_callback, long stack_ref_callback, long object_ref_callback, long user_data) {
        return alice._native.jvmti.jvmtiInterface_1_.IterateOverReachableObjects.invoke(JNIUtil.getJVMTIEnv(), heap_root_callback, stack_ref_callback, object_ref_callback, user_data);
    }
}
