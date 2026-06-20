package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetNativeMethodPrefix) (jvmtiEnv* env, const char* prefix);
public final class SetNativeMethodPrefix {

    private SetNativeMethodPrefix() {
    }

    public static int invoke(long prefix) {
        return alice._native.jvmti.jvmtiInterface_1_.SetNativeMethodPrefix.invoke(JNIUtil.getJVMTIEnv(), prefix);
    }
}
