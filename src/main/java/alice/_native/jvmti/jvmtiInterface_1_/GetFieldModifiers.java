package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetFieldModifiers) (jvmtiEnv* env, jclass klass, jfieldID field, jint* modifiers_ptr);
public final class GetFieldModifiers {

    private static final long code_base = JVMTINativeCall.create(GetFieldModifiers.class, "()I", JVMTINativeCall.GET_FIELD_MODIFIERS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetFieldModifiers() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long field, long modifiers_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, field);
        JVMTINativeCall.setArg(code_base, 2, modifiers_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long field, long modifiers_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, field, modifiers_ptr);
    }
}
