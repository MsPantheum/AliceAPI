package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IterateThroughHeap) (jvmtiEnv* env, jint heap_filter, jclass klass, const jvmtiHeapCallbacks* callbacks, const void* user_data);
public final class IterateThroughHeap {

    private IterateThroughHeap() {
    }

    public static int invoke(int heap_filter, long klass, long callbacks, long user_data) {
        return alice._native.jvmti.jvmtiInterface_1_.IterateThroughHeap.invoke(JNIUtil.getJVMTIEnv(), heap_filter, klass, callbacks, user_data);
    }
}
