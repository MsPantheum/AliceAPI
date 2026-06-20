package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *AddCapabilities) (jvmtiEnv* env, const jvmtiCapabilities* capabilities_ptr);
public final class AddCapabilities {

    private static final long code_base = JVMTINativeCall.create(AddCapabilities.class, "()I", JVMTINativeCall.ADD_CAPABILITIES, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private AddCapabilities() {
    }

    public synchronized static int invoke(long JVMTIEnv, long capabilities_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, capabilities_ptr);
        return holder();
    }

    public synchronized static int invoke(long capabilities_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), capabilities_ptr);
    }
}
