package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetClassSignature) (jvmtiEnv* env, jclass klass, char** signature_ptr, char** generic_ptr);
public final class GetClassSignature {

    private GetClassSignature() {
    }

    public static int invoke(long klass, long signature_ptr, long generic_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetClassSignature.invoke(JNIUtil.getJVMTIEnv(), klass, signature_ptr, generic_ptr);
    }
}
