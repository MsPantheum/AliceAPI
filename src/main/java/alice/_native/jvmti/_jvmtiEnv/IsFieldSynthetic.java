package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IsFieldSynthetic) (jvmtiEnv* env, jclass klass, jfieldID field, jboolean* is_synthetic_ptr);
public final class IsFieldSynthetic {

    private IsFieldSynthetic() {
    }

    public static int invoke(long klass, long field, long is_synthetic_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.IsFieldSynthetic.invoke(JNIUtil.getJVMTIEnv(), klass, field, is_synthetic_ptr);
    }
}
