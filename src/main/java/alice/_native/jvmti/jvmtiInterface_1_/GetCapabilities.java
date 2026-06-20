package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetCapabilities) (jvmtiEnv* env, jvmtiCapabilities* capabilities_ptr);
public final class GetCapabilities {

    private static final long code_base = JVMTINativeCall.create(GetCapabilities.class, "()I", JVMTINativeCall.GET_CAPABILITIES, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetCapabilities() {
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
