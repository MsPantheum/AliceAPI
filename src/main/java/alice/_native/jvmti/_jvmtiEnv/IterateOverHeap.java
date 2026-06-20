package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IterateOverHeap) (jvmtiEnv* env, jvmtiHeapObjectFilter object_filter, jvmtiHeapObjectCallback heap_object_callback, const void* user_data);
public final class IterateOverHeap {

    private IterateOverHeap() {
    }

    public static int invoke(int object_filter, long heap_object_callback, long user_data) {
        return alice._native.jvmti.jvmtiInterface_1_.IterateOverHeap.invoke(JNIUtil.getJVMTIEnv(), object_filter, heap_object_callback, user_data);
    }
}
