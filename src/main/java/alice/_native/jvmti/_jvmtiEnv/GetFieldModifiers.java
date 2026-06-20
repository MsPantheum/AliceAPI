package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetFieldModifiers) (jvmtiEnv* env, jclass klass, jfieldID field, jint* modifiers_ptr);
public final class GetFieldModifiers {

    private GetFieldModifiers() {
    }

    public static int invoke(long klass, long field, long modifiers_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetFieldModifiers.invoke(JNIUtil.getJVMTIEnv(), klass, field, modifiers_ptr);
    }
}
