package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IsFieldSynthetic) (jvmtiEnv* env, jclass klass, jfieldID field, jboolean* is_synthetic_ptr);
public final class IsFieldSynthetic {

    private static final long code_base = JVMTINativeCall.create(IsFieldSynthetic.class, "()I", JVMTINativeCall.IS_FIELD_SYNTHETIC, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private IsFieldSynthetic() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long field, long is_synthetic_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, field);
        JVMTINativeCall.setArg(code_base, 2, is_synthetic_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long field, long is_synthetic_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, field, is_synthetic_ptr);
    }
}
