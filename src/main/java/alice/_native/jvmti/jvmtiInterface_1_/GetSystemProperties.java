package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetSystemProperties) (jvmtiEnv* env, jint* count_ptr, char*** property_ptr);
public final class GetSystemProperties {

    private static final long code_base = JVMTINativeCall.create(GetSystemProperties.class, "()I", JVMTINativeCall.GET_SYSTEM_PROPERTIES, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetSystemProperties() {
    }

    public synchronized static int invoke(long JVMTIEnv, long count_ptr, long property_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, count_ptr);
        JVMTINativeCall.setArg(code_base, 1, property_ptr);
        return holder();
    }

    public synchronized static int invoke(long count_ptr, long property_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), count_ptr, property_ptr);
    }
}
