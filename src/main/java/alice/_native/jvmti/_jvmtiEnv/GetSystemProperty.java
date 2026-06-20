package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetSystemProperty) (jvmtiEnv* env, const char* property, char** value_ptr);
public final class GetSystemProperty {

    private GetSystemProperty() {
    }

    public static int invoke(long property, long value_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetSystemProperty.invoke(JNIUtil.getJVMTIEnv(), property, value_ptr);
    }
}
