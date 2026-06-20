package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetClassModifiers) (jvmtiEnv* env, jclass klass, jint* modifiers_ptr);
public final class GetClassModifiers {

    private static final long code_base = JVMTINativeCall.create(GetClassModifiers.class, "()I", JVMTINativeCall.GET_CLASS_MODIFIERS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetClassModifiers() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long modifiers_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, modifiers_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long modifiers_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, modifiers_ptr);
    }
}
