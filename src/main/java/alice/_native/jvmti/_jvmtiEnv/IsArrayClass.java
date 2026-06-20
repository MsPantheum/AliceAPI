package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IsArrayClass) (jvmtiEnv* env, jclass klass, jboolean* is_array_class_ptr);
public final class IsArrayClass {

    private IsArrayClass() {
    }

    public static int invoke(long klass, long is_array_class_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.IsArrayClass.invoke(JNIUtil.getJVMTIEnv(), klass, is_array_class_ptr);
    }
}
