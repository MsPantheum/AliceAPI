package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetClassModifiers) (jvmtiEnv* env, jclass klass, jint* modifiers_ptr);
public final class GetClassModifiers {

    private GetClassModifiers() {
    }

    public static int invoke(long klass, long modifiers_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetClassModifiers.invoke(JNIUtil.getJVMTIEnv(), klass, modifiers_ptr);
    }
}
