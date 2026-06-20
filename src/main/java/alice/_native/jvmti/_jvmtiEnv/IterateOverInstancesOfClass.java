package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IterateOverInstancesOfClass) (jvmtiEnv* env, jclass klass, jvmtiHeapObjectFilter object_filter, jvmtiHeapObjectCallback heap_object_callback, const void* user_data);
public final class IterateOverInstancesOfClass {

    private IterateOverInstancesOfClass() {
    }

    public static int invoke(long klass, int object_filter, long heap_object_callback, long user_data) {
        return alice._native.jvmti.jvmtiInterface_1_.IterateOverInstancesOfClass.invoke(JNIUtil.getJVMTIEnv(), klass, object_filter, heap_object_callback, user_data);
    }
}
