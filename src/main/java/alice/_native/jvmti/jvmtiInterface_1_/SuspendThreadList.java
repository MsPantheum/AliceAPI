package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SuspendThreadList) (jvmtiEnv* env, jint request_count, const jthread* request_list, jvmtiError* results);
public final class SuspendThreadList {

    private static final long code_base = JVMTINativeCall.create(SuspendThreadList.class, "()I", JVMTINativeCall.SUSPEND_THREAD_LIST, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private SuspendThreadList() {
    }

    public synchronized static int invoke(long JVMTIEnv, int request_count, long request_list, long results) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, request_count);
        JVMTINativeCall.setArg(code_base, 1, request_list);
        JVMTINativeCall.setArg(code_base, 2, results);
        return holder();
    }

    public synchronized static int invoke(int request_count, long request_list, long results) {
        return invoke(JNIUtil.getJVMTIEnv(), request_count, request_list, results);
    }
}
