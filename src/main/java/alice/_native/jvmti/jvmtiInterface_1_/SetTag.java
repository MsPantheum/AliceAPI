package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetTag) (jvmtiEnv* env, jobject object, jlong tag);
public final class SetTag {

    private static final long code_base = JVMTINativeCall.create(SetTag.class, "()I", JVMTINativeCall.SET_TAG, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private SetTag() {
    }

    public synchronized static int invoke(long JVMTIEnv, long object, long tag) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, object);
        JVMTINativeCall.setArg(code_base, 1, tag);
        return holder();
    }

    public synchronized static int invoke(long object, long tag) {
        return invoke(JNIUtil.getJVMTIEnv(), object, tag);
    }
}
