package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetCapabilities) (jvmtiEnv* env, jvmtiCapabilities* capabilities_ptr);
public final class GetCapabilities {

    private GetCapabilities() {
    }

    public static int invoke(long capabilities_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetCapabilities.invoke(JNIUtil.getJVMTIEnv(), capabilities_ptr);
    }
}
