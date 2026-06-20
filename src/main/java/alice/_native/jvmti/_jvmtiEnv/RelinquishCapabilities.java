package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *RelinquishCapabilities) (jvmtiEnv* env, const jvmtiCapabilities* capabilities_ptr);
public final class RelinquishCapabilities {

    private RelinquishCapabilities() {
    }

    public static int invoke(long capabilities_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.RelinquishCapabilities.invoke(JNIUtil.getJVMTIEnv(), capabilities_ptr);
    }
}
