package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *AddCapabilities) (jvmtiEnv* env, const jvmtiCapabilities* capabilities_ptr);
public final class AddCapabilities {

    private AddCapabilities() {
    }

    public static int invoke(long capabilities_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.AddCapabilities.invoke(JNIUtil.getJVMTIEnv(), capabilities_ptr);
    }
}
