package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetVersionNumber) (jvmtiEnv* env, jint* version_ptr);
public final class GetVersionNumber {

    private static final long code_base = JVMTINativeCall.create(GetVersionNumber.class, "()I", JVMTINativeCall.GET_VERSION_NUMBER, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetVersionNumber() {
    }

    public synchronized static int invoke(long JVMTIEnv, long version_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, version_ptr);
        return holder();
    }

    public synchronized static int invoke(long version_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), version_ptr);
    }
}
