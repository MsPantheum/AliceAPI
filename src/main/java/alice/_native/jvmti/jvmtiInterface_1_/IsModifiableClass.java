package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IsModifiableClass) (jvmtiEnv* env, jclass klass, jboolean* is_modifiable_class_ptr);
public final class IsModifiableClass {

    private static final long code_base = JVMTINativeCall.create(IsModifiableClass.class, "()I", JVMTINativeCall.IS_MODIFIABLE_CLASS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private IsModifiableClass() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long is_modifiable_class_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, is_modifiable_class_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long is_modifiable_class_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, is_modifiable_class_ptr);
    }
}
