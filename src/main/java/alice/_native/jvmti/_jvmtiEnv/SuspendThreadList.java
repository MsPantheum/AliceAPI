package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SuspendThreadList) (jvmtiEnv* env, jint request_count, const jthread* request_list, jvmtiError* results);
public final class SuspendThreadList {

    private SuspendThreadList() {
    }

    public static int invoke(int request_count, long request_list, long results) {
        return alice._native.jvmti.jvmtiInterface_1_.SuspendThreadList.invoke(JNIUtil.getJVMTIEnv(), request_count, request_list, results);
    }
}
