package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IterateOverHeap) (jvmtiEnv* env, jvmtiHeapObjectFilter object_filter, jvmtiHeapObjectCallback heap_object_callback, const void* user_data);
public final class IterateOverHeap {

    private static final long code_base = JVMTINativeCall.create(IterateOverHeap.class, "()I", JVMTINativeCall.ITERATE_OVER_HEAP, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private IterateOverHeap() {
    }

    public synchronized static int invoke(long JVMTIEnv, int object_filter, long heap_object_callback, long user_data) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, object_filter);
        JVMTINativeCall.setArg(code_base, 1, heap_object_callback);
        JVMTINativeCall.setArg(code_base, 2, user_data);
        return holder();
    }

    public synchronized static int invoke(int object_filter, long heap_object_callback, long user_data) {
        return invoke(JNIUtil.getJVMTIEnv(), object_filter, heap_object_callback, user_data);
    }
}
