package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ClearBreakpoint) (jvmtiEnv* env, jmethodID method, jlocation location);
public final class ClearBreakpoint {

    private static final long code_base = JVMTINativeCall.create(ClearBreakpoint.class, "()I", JVMTINativeCall.CLEAR_BREAKPOINT, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private ClearBreakpoint() {
    }

    public synchronized static int invoke(long JVMTIEnv, long method, long location) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, method);
        JVMTINativeCall.setArg(code_base, 1, location);
        return holder();
    }

    public synchronized static int invoke(long method, long location) {
        return invoke(JNIUtil.getJVMTIEnv(), method, location);
    }
}
