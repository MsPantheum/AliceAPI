package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetJNIFunctionTable) (jvmtiEnv* env, jniNativeInterface** function_table);
public final class GetJNIFunctionTable {

    private GetJNIFunctionTable() {
    }

    public static int invoke(long function_table) {
        return alice._native.jvmti.jvmtiInterface_1_.GetJNIFunctionTable.invoke(JNIUtil.getJVMTIEnv(), function_table);
    }
}
