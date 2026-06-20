package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IterateOverObjectsReachableFromObject) (jvmtiEnv* env, jobject object, jvmtiObjectReferenceCallback object_reference_callback, const void* user_data);
public final class IterateOverObjectsReachableFromObject {

    private static final long code_base = JVMTINativeCall.create(IterateOverObjectsReachableFromObject.class, "()I", JVMTINativeCall.ITERATE_OVER_OBJECTS_REACHABLE_FROM_OBJECT, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private IterateOverObjectsReachableFromObject() {
    }

    public synchronized static int invoke(long JVMTIEnv, long object, long object_reference_callback, long user_data) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, object);
        JVMTINativeCall.setArg(code_base, 1, object_reference_callback);
        JVMTINativeCall.setArg(code_base, 2, user_data);
        return holder();
    }

    public synchronized static int invoke(long object, long object_reference_callback, long user_data) {
        return invoke(JNIUtil.getJVMTIEnv(), object, object_reference_callback, user_data);
    }
}
