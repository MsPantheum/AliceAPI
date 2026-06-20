package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetSystemProperties) (jvmtiEnv* env, jint* count_ptr, char*** property_ptr);
public final class GetSystemProperties {

    private GetSystemProperties() {
    }

    public static int invoke(long count_ptr, long property_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetSystemProperties.invoke(JNIUtil.getJVMTIEnv(), count_ptr, property_ptr);
    }
}
