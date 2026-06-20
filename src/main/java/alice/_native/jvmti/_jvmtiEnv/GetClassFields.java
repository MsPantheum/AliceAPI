package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetClassFields) (jvmtiEnv* env, jclass klass, jint* field_count_ptr, jfieldID** fields_ptr);
public final class GetClassFields {

    private GetClassFields() {
    }

    public static int invoke(long klass, long field_count_ptr, long fields_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetClassFields.invoke(JNIUtil.getJVMTIEnv(), klass, field_count_ptr, fields_ptr);
    }
}
