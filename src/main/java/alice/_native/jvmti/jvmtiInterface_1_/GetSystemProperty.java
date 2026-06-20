package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetSystemProperty) (jvmtiEnv* env, const char* property, char** value_ptr);
public final class GetSystemProperty {

    private static final long code_base = JVMTINativeCall.create(GetSystemProperty.class, "()I", JVMTINativeCall.GET_SYSTEM_PROPERTY, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetSystemProperty() {
    }

    public synchronized static int invoke(long JVMTIEnv, long property, long value_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, property);
        JVMTINativeCall.setArg(code_base, 1, value_ptr);
        return holder();
    }

    public synchronized static int invoke(long property, long value_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), property, value_ptr);
    }
}
