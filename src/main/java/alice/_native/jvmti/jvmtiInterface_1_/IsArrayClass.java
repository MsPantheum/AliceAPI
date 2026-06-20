package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IsArrayClass) (jvmtiEnv* env, jclass klass, jboolean* is_array_class_ptr);
public final class IsArrayClass {

    private static final long code_base = JVMTINativeCall.create(IsArrayClass.class, "()I", JVMTINativeCall.IS_ARRAY_CLASS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private IsArrayClass() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long is_array_class_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, is_array_class_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long is_array_class_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, is_array_class_ptr);
    }
}
