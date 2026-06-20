package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetFieldName) (jvmtiEnv* env, jclass klass, jfieldID field, char** name_ptr, char** signature_ptr, char** generic_ptr);
public final class GetFieldName {

    private GetFieldName() {
    }

    public static int invoke(long klass, long field, long name_ptr, long signature_ptr, long generic_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetFieldName.invoke(JNIUtil.getJVMTIEnv(), klass, field, name_ptr, signature_ptr, generic_ptr);
    }
}
