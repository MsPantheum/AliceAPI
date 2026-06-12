package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jobject (JNICALL *NewLocalRef)(JNIEnv *env, jobject ref);
public final class NewLocalRef {

    private static final long code_base = JNINativeCall.create(NewLocalRef.class, "()J", JNINativeCall.NEW_LOCAL_REF, 1);

    private static native long holder();

    private NewLocalRef() {
    }

    public synchronized static long invoke(long JNIEnv, long ref) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, ref);
        return holder();
    }

    public synchronized static long invoke(long ref) {
        return invoke(JNIUtil.getJNIEnv(), ref);
    }
}
