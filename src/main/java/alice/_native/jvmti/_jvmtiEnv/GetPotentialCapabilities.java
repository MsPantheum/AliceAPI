package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetPotentialCapabilities) (jvmtiEnv* env, jvmtiCapabilities* capabilities_ptr);
public final class GetPotentialCapabilities {

    private GetPotentialCapabilities() {
    }

    public static int invoke(long capabilities_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetPotentialCapabilities.invoke(JNIUtil.getJVMTIEnv(), capabilities_ptr);
    }
}
