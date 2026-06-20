package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetSystemProperty) (jvmtiEnv* env, const char* property, const char* value);
public final class SetSystemProperty {

    private static final long code_base = JVMTINativeCall.create(SetSystemProperty.class, "()I", JVMTINativeCall.SET_SYSTEM_PROPERTY, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private SetSystemProperty() {
    }

    public synchronized static int invoke(long JVMTIEnv, long property, long value) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, property);
        JVMTINativeCall.setArg(code_base, 1, value);
        return holder();
    }

    public synchronized static int invoke(long property, long value) {
        return invoke(JNIUtil.getJVMTIEnv(), property, value);
    }
}
