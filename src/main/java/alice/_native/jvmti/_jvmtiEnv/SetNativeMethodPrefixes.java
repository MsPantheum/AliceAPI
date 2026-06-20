package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetNativeMethodPrefixes) (jvmtiEnv* env, jint prefix_count, char** prefixes);
public final class SetNativeMethodPrefixes {

    private SetNativeMethodPrefixes() {
    }

    public static int invoke(int prefix_count, long prefixes) {
        return alice._native.jvmti.jvmtiInterface_1_.SetNativeMethodPrefixes.invoke(JNIUtil.getJVMTIEnv(), prefix_count, prefixes);
    }
}
