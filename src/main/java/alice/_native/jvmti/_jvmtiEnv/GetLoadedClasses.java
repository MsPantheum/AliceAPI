package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetLoadedClasses) (jvmtiEnv* env, jint* class_count_ptr, jclass** classes_ptr);
public final class GetLoadedClasses {

    private GetLoadedClasses() {
    }

    public static int invoke(long class_count_ptr, long classes_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetLoadedClasses.invoke(JNIUtil.getJVMTIEnv(), class_count_ptr, classes_ptr);
    }
}
