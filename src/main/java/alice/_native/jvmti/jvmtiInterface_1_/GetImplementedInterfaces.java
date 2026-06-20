package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetImplementedInterfaces) (jvmtiEnv* env, jclass klass, jint* interface_count_ptr, jclass** interfaces_ptr);
public final class GetImplementedInterfaces {

    private static final long code_base = JVMTINativeCall.create(GetImplementedInterfaces.class, "()I", JVMTINativeCall.GET_IMPLEMENTED_INTERFACES, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetImplementedInterfaces() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long interface_count_ptr, long interfaces_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, interface_count_ptr);
        JVMTINativeCall.setArg(code_base, 2, interfaces_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long interface_count_ptr, long interfaces_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, interface_count_ptr, interfaces_ptr);
    }
}
