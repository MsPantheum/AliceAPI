package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetClassSignature) (jvmtiEnv* env, jclass klass, char** signature_ptr, char** generic_ptr);
public final class GetClassSignature {

    private static final long code_base = JVMTINativeCall.create(GetClassSignature.class, "()I", JVMTINativeCall.GET_CLASS_SIGNATURE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetClassSignature() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long signature_ptr, long generic_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, signature_ptr);
        JVMTINativeCall.setArg(code_base, 2, generic_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long signature_ptr, long generic_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, signature_ptr, generic_ptr);
    }
}
