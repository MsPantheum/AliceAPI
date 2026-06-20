package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IterateThroughHeap) (jvmtiEnv* env, jint heap_filter, jclass klass, const jvmtiHeapCallbacks* callbacks, const void* user_data);
public final class IterateThroughHeap {

    private static final long code_base = JVMTINativeCall.create(IterateThroughHeap.class, "()I", JVMTINativeCall.ITERATE_THROUGH_HEAP, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private IterateThroughHeap() {
    }

    public synchronized static int invoke(long JVMTIEnv, int heap_filter, long klass, long callbacks, long user_data) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, heap_filter);
        JVMTINativeCall.setArg(code_base, 1, klass);
        JVMTINativeCall.setArg(code_base, 2, callbacks);
        JVMTINativeCall.setArg(code_base, 3, user_data);
        return holder();
    }

    public synchronized static int invoke(int heap_filter, long klass, long callbacks, long user_data) {
        return invoke(JNIUtil.getJVMTIEnv(), heap_filter, klass, callbacks, user_data);
    }
}
