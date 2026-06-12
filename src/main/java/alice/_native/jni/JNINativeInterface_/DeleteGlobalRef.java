package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//void (JNICALL *DeleteGlobalRef)(JNIEnv *env, jobject gref);
public final class DeleteGlobalRef {

    private static final long code_base = JNINativeCall.create(DeleteGlobalRef.class, "()V", JNINativeCall.DELETE_GLOBAL_REF, 1);

    private static native void holder();

    private DeleteGlobalRef() {
    }

    public synchronized static void invoke(long JNIEnv, long gref) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, gref);
        holder();
    }

    public synchronized static void invoke(long gref) {
        invoke(JNIUtil.getJNIEnv(), gref);
    }
}
