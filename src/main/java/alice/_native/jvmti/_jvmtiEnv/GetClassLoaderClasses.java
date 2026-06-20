package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetClassLoaderClasses) (jvmtiEnv* env, jobject initiating_loader, jint* class_count_ptr, jclass** classes_ptr);
public final class GetClassLoaderClasses {

    private GetClassLoaderClasses() {
    }

    public static int invoke(long initiating_loader, long class_count_ptr, long classes_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetClassLoaderClasses.invoke(JNIUtil.getJVMTIEnv(), initiating_loader, class_count_ptr, classes_ptr);
    }
}
