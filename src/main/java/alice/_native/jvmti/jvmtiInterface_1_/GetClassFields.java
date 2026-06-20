package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetClassFields) (jvmtiEnv* env, jclass klass, jint* field_count_ptr, jfieldID** fields_ptr);
public final class GetClassFields {

    private static final long code_base = JVMTINativeCall.create(GetClassFields.class, "()I", JVMTINativeCall.GET_CLASS_FIELDS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetClassFields() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long field_count_ptr, long fields_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, field_count_ptr);
        JVMTINativeCall.setArg(code_base, 2, fields_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long field_count_ptr, long fields_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, field_count_ptr, fields_ptr);
    }
}
