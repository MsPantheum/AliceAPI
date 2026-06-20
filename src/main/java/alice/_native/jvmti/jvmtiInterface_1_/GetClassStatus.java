package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetClassStatus) (jvmtiEnv* env, jclass klass, jint* status_ptr);
public final class GetClassStatus {

    private static final long code_base = JVMTINativeCall.create(GetClassStatus.class, "()I", JVMTINativeCall.GET_CLASS_STATUS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetClassStatus() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long status_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, status_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long status_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, status_ptr);
    }
}
