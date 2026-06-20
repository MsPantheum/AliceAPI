package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetFieldDeclaringClass) (jvmtiEnv* env, jclass klass, jfieldID field, jclass* declaring_class_ptr);
public final class GetFieldDeclaringClass {

    private static final long code_base = JVMTINativeCall.create(GetFieldDeclaringClass.class, "()I", JVMTINativeCall.GET_FIELD_DECLARING_CLASS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetFieldDeclaringClass() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long field, long declaring_class_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, field);
        JVMTINativeCall.setArg(code_base, 2, declaring_class_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long field, long declaring_class_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, field, declaring_class_ptr);
    }
}
