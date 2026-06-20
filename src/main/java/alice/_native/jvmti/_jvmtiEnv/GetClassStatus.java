package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetClassStatus) (jvmtiEnv* env, jclass klass, jint* status_ptr);
public final class GetClassStatus {

    private GetClassStatus() {
    }

    public static int invoke(long klass, long status_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetClassStatus.invoke(JNIUtil.getJVMTIEnv(), klass, status_ptr);
    }
}
