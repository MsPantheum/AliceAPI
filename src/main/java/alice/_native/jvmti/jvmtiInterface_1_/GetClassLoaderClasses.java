package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetClassLoaderClasses) (jvmtiEnv* env, jobject initiating_loader, jint* class_count_ptr, jclass** classes_ptr);
public final class GetClassLoaderClasses {

    private static final long code_base = JVMTINativeCall.create(GetClassLoaderClasses.class, "()I", JVMTINativeCall.GET_CLASS_LOADER_CLASSES, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetClassLoaderClasses() {
    }

    public synchronized static int invoke(long JVMTIEnv, long initiating_loader, long class_count_ptr, long classes_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, initiating_loader);
        JVMTINativeCall.setArg(code_base, 1, class_count_ptr);
        JVMTINativeCall.setArg(code_base, 2, classes_ptr);
        return holder();
    }

    public synchronized static int invoke(long initiating_loader, long class_count_ptr, long classes_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), initiating_loader, class_count_ptr, classes_ptr);
    }
}
