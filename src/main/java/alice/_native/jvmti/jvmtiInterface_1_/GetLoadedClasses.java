package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetLoadedClasses) (jvmtiEnv* env, jint* class_count_ptr, jclass** classes_ptr);
public final class GetLoadedClasses {

    private static final long code_base = JVMTINativeCall.create(GetLoadedClasses.class, "()I", JVMTINativeCall.GET_LOADED_CLASSES, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetLoadedClasses() {
    }

    public synchronized static int invoke(long JVMTIEnv, long class_count_ptr, long classes_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, class_count_ptr);
        JVMTINativeCall.setArg(code_base, 1, classes_ptr);
        return holder();
    }

    public synchronized static int invoke(long class_count_ptr, long classes_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), class_count_ptr, classes_ptr);
    }
}
