package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IsInterface) (jvmtiEnv* env, jclass klass, jboolean* is_interface_ptr);
public final class IsInterface {

    private static final long code_base = JVMTINativeCall.create(IsInterface.class, "()I", JVMTINativeCall.IS_INTERFACE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private IsInterface() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long is_interface_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, is_interface_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long is_interface_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, is_interface_ptr);
    }
}
