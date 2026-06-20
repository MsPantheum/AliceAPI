package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IsModifiableClass) (jvmtiEnv* env, jclass klass, jboolean* is_modifiable_class_ptr);
public final class IsModifiableClass {

    private IsModifiableClass() {
    }

    public static int invoke(long klass, long is_modifiable_class_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.IsModifiableClass.invoke(JNIUtil.getJVMTIEnv(), klass, is_modifiable_class_ptr);
    }
}
