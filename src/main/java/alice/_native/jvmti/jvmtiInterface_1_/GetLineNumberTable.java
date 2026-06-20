package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetLineNumberTable) (jvmtiEnv* env, jmethodID method, jint* entry_count_ptr, jvmtiLineNumberEntry** table_ptr);
public final class GetLineNumberTable {

    private static final long code_base = JVMTINativeCall.create(GetLineNumberTable.class, "()I", JVMTINativeCall.GET_LINE_NUMBER_TABLE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetLineNumberTable() {
    }

    public synchronized static int invoke(long JVMTIEnv, long method, long entry_count_ptr, long table_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, method);
        JVMTINativeCall.setArg(code_base, 1, entry_count_ptr);
        JVMTINativeCall.setArg(code_base, 2, table_ptr);
        return holder();
    }

    public synchronized static int invoke(long method, long entry_count_ptr, long table_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), method, entry_count_ptr, table_ptr);
    }
}
