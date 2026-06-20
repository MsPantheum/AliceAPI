package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetAvailableProcessors) (jvmtiEnv* env, jint* processor_count_ptr);
public final class GetAvailableProcessors {

    private GetAvailableProcessors() {
    }

    public static int invoke(long processor_count_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetAvailableProcessors.invoke(JNIUtil.getJVMTIEnv(), processor_count_ptr);
    }
}
