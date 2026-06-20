package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetFieldDeclaringClass) (jvmtiEnv* env, jclass klass, jfieldID field, jclass* declaring_class_ptr);
public final class GetFieldDeclaringClass {

    private GetFieldDeclaringClass() {
    }

    public static int invoke(long klass, long field, long declaring_class_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetFieldDeclaringClass.invoke(JNIUtil.getJVMTIEnv(), klass, field, declaring_class_ptr);
    }
}
