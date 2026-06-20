package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetPhase) (jvmtiEnv* env, jvmtiPhase* phase_ptr);
public final class GetPhase {

    private static final long code_base = JVMTINativeCall.create(GetPhase.class, "()I", JVMTINativeCall.GET_PHASE, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetPhase() {
    }

    public synchronized static int invoke(long JVMTIEnv, long phase_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, phase_ptr);
        return holder();
    }

    public synchronized static int invoke(long phase_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), phase_ptr);
    }
}
