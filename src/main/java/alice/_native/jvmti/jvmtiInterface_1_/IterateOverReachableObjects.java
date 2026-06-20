package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IterateOverReachableObjects) (jvmtiEnv* env, jvmtiHeapRootCallback heap_root_callback, jvmtiStackReferenceCallback stack_ref_callback, jvmtiObjectReferenceCallback object_ref_callback, const void* user_data);
public final class IterateOverReachableObjects {

    private static final long code_base = JVMTINativeCall.create(IterateOverReachableObjects.class, "()I", JVMTINativeCall.ITERATE_OVER_REACHABLE_OBJECTS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private IterateOverReachableObjects() {
    }

    public synchronized static int invoke(long JVMTIEnv, long heap_root_callback, long stack_ref_callback, long object_ref_callback, long user_data) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, heap_root_callback);
        JVMTINativeCall.setArg(code_base, 1, stack_ref_callback);
        JVMTINativeCall.setArg(code_base, 2, object_ref_callback);
        JVMTINativeCall.setArg(code_base, 3, user_data);
        return holder();
    }

    public synchronized static int invoke(long heap_root_callback, long stack_ref_callback, long object_ref_callback, long user_data) {
        return invoke(JNIUtil.getJVMTIEnv(), heap_root_callback, stack_ref_callback, object_ref_callback, user_data);
    }
}
