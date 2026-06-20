package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetFieldAccessWatch) (jvmtiEnv* env, jclass klass, jfieldID field);
public final class SetFieldAccessWatch {

    private static final long code_base = JVMTINativeCall.create(SetFieldAccessWatch.class, "()I", JVMTINativeCall.SET_FIELD_ACCESS_WATCH, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private SetFieldAccessWatch() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long field) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, field);
        return holder();
    }

    public synchronized static int invoke(long klass, long field) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, field);
    }
}
