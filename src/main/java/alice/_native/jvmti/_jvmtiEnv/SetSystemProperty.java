package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetSystemProperty) (jvmtiEnv* env, const char* property, const char* value);
public final class SetSystemProperty {

    private SetSystemProperty() {
    }

    public static int invoke(long property, long value) {
        return alice._native.jvmti.jvmtiInterface_1_.SetSystemProperty.invoke(JNIUtil.getJVMTIEnv(), property, value);
    }
}
