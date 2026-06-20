package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetBreakpoint) (jvmtiEnv* env, jmethodID method, jlocation location);
public final class SetBreakpoint {

    private static final long code_base = JVMTINativeCall.create(SetBreakpoint.class, "()I", JVMTINativeCall.SET_BREAKPOINT, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private SetBreakpoint() {
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
