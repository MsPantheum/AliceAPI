package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetLineNumberTable) (jvmtiEnv* env, jmethodID method, jint* entry_count_ptr, jvmtiLineNumberEntry** table_ptr);
public final class GetLineNumberTable {

    private GetLineNumberTable() {
    }

    public static int invoke(long method, long entry_count_ptr, long table_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetLineNumberTable.invoke(JNIUtil.getJVMTIEnv(), method, entry_count_ptr, table_ptr);
    }
}
