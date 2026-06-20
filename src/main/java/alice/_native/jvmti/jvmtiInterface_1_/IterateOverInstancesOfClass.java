package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IterateOverInstancesOfClass) (jvmtiEnv* env, jclass klass, jvmtiHeapObjectFilter object_filter, jvmtiHeapObjectCallback heap_object_callback, const void* user_data);
public final class IterateOverInstancesOfClass {

    private static final long code_base = JVMTINativeCall.create(IterateOverInstancesOfClass.class, "()I", JVMTINativeCall.ITERATE_OVER_INSTANCES_OF_CLASS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private IterateOverInstancesOfClass() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, int object_filter, long heap_object_callback, long user_data) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, object_filter);
        JVMTINativeCall.setArg(code_base, 2, heap_object_callback);
        JVMTINativeCall.setArg(code_base, 3, user_data);
        return holder();
    }

    public synchronized static int invoke(long klass, int object_filter, long heap_object_callback, long user_data) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, object_filter, heap_object_callback, user_data);
    }
}
