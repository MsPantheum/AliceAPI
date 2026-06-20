package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetFieldName) (jvmtiEnv* env, jclass klass, jfieldID field, char** name_ptr, char** signature_ptr, char** generic_ptr);
public final class GetFieldName {

    private static final long code_base = JVMTINativeCall.create(GetFieldName.class, "()I", JVMTINativeCall.GET_FIELD_NAME, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetFieldName() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long field, long name_ptr, long signature_ptr, long generic_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, field);
        JVMTINativeCall.setArg(code_base, 2, name_ptr);
        JVMTINativeCall.setArg(code_base, 3, signature_ptr);
        JVMTINativeCall.setArg(code_base, 4, generic_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long field, long name_ptr, long signature_ptr, long generic_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, field, name_ptr, signature_ptr, generic_ptr);
    }
}
