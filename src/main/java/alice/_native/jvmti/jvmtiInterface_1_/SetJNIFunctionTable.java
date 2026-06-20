package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetJNIFunctionTable) (jvmtiEnv* env, const jniNativeInterface* function_table);
public final class SetJNIFunctionTable {

    private static final long code_base = JVMTINativeCall.create(SetJNIFunctionTable.class, "()I", JVMTINativeCall.SET_JNI_FUNCTION_TABLE, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private SetJNIFunctionTable() {
    }

    public synchronized static int invoke(long JVMTIEnv, long function_table) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, function_table);
        return holder();
    }

    public synchronized static int invoke(long function_table) {
        return invoke(JNIUtil.getJVMTIEnv(), function_table);
    }
}
