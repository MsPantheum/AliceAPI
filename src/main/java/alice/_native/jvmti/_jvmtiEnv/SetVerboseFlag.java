package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetVerboseFlag) (jvmtiEnv* env, jvmtiVerboseFlag flag, jboolean value);
public final class SetVerboseFlag {

    private SetVerboseFlag() {
    }

    public static int invoke(int flag, boolean value) {
        return alice._native.jvmti.jvmtiInterface_1_.SetVerboseFlag.invoke(JNIUtil.getJVMTIEnv(), flag, value);
    }
}
