package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetLocalVariableTable) (jvmtiEnv* env, jmethodID method, jint* entry_count_ptr, jvmtiLocalVariableEntry** table_ptr);
public final class GetLocalVariableTable {

    private GetLocalVariableTable() {
    }

    public static int invoke(long method, long entry_count_ptr, long table_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetLocalVariableTable.invoke(JNIUtil.getJVMTIEnv(), method, entry_count_ptr, table_ptr);
    }
}
