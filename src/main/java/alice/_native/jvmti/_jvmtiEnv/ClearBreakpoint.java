package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ClearBreakpoint) (jvmtiEnv* env, jmethodID method, jlocation location);
public final class ClearBreakpoint {

    private ClearBreakpoint() {
    }

    public static int invoke(long method, long location) {
        return alice._native.jvmti.jvmtiInterface_1_.ClearBreakpoint.invoke(JNIUtil.getJVMTIEnv(), method, location);
    }
}
