package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetSourceFileName) (jvmtiEnv* env, jclass klass, char** source_name_ptr);
public final class GetSourceFileName {

    private GetSourceFileName() {
    }

    public static int invoke(long klass, long source_name_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetSourceFileName.invoke(JNIUtil.getJVMTIEnv(), klass, source_name_ptr);
    }
}
