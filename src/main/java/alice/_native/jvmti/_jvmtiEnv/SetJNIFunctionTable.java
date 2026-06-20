package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetJNIFunctionTable) (jvmtiEnv* env, const jniNativeInterface* function_table);
public final class SetJNIFunctionTable {

    private SetJNIFunctionTable() {
    }

    public static int invoke(long function_table) {
        return alice._native.jvmti.jvmtiInterface_1_.SetJNIFunctionTable.invoke(JNIUtil.getJVMTIEnv(), function_table);
    }
}
