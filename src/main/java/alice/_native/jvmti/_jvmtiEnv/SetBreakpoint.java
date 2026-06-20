package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetBreakpoint) (jvmtiEnv* env, jmethodID method, jlocation location);
public final class SetBreakpoint {

    private SetBreakpoint() {
    }

    public static int invoke(long method, long location) {
        return alice._native.jvmti.jvmtiInterface_1_.SetBreakpoint.invoke(JNIUtil.getJVMTIEnv(), method, location);
    }
}
