package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IsMethodObsolete) (jvmtiEnv* env, jmethodID method, jboolean* is_obsolete_ptr);
public final class IsMethodObsolete {

    private static final long code_base = JVMTINativeCall.create(IsMethodObsolete.class, "()I", JVMTINativeCall.IS_METHOD_OBSOLETE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private IsMethodObsolete() {
    }

    public synchronized static int invoke(long JVMTIEnv, long method, long is_obsolete_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, method);
        JVMTINativeCall.setArg(code_base, 1, is_obsolete_ptr);
        return holder();
    }

    public synchronized static int invoke(long method, long is_obsolete_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), method, is_obsolete_ptr);
    }
}
