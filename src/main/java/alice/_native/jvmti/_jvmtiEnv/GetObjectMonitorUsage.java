package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetObjectMonitorUsage) (jvmtiEnv* env, jobject object, jvmtiMonitorUsage* info_ptr);
public final class GetObjectMonitorUsage {

    private GetObjectMonitorUsage() {
    }

    public static int invoke(long object, long info_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetObjectMonitorUsage.invoke(JNIUtil.getJVMTIEnv(), object, info_ptr);
    }
}
