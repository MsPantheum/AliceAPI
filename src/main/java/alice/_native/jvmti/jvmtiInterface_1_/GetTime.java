package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetTime) (jvmtiEnv* env, jlong* nanos_ptr);
public final class GetTime {

    private static final long code_base = JVMTINativeCall.create(GetTime.class, "()I", JVMTINativeCall.GET_TIME, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetTime() {
    }

    public synchronized static int invoke(long JVMTIEnv, long nanos_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, nanos_ptr);
        return holder();
    }

    public synchronized static int invoke(long nanos_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), nanos_ptr);
    }
}
