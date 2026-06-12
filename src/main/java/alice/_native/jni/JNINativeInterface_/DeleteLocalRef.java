package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//void (JNICALL *DeleteLocalRef)(JNIEnv *env, jobject obj);
public final class DeleteLocalRef {

    private static final long code_base = JNINativeCall.create(DeleteLocalRef.class, "()V", JNINativeCall.DELETE_LOCAL_REF, 1);

    private static native void holder();

    private DeleteLocalRef() {
    }

    public synchronized static void invoke(long JNIEnv, long obj) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, obj);
        holder();
    }

    public synchronized static void invoke(long obj) {
        invoke(JNIUtil.getJNIEnv(), obj);
    }
}
