package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//void (JNICALL *ReleaseStringUTFChars)(JNIEnv *env, jstring string, const char *utf);
public final class ReleaseStringUTFChars {

    private static final long code_base = JNINativeCall.create(ReleaseStringUTFChars.class, "()V", JNINativeCall.RELEASE_STRING_UTF_CHARS, 2);

    private static native void holder();

    private ReleaseStringUTFChars() {
    }

    public synchronized static void invoke(long JNIEnv, long string, long utf) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, string);
        JNINativeCall.setArg(code_base, 1, utf);
        holder();
    }

    public synchronized static void invoke(long string, long utf) {
        invoke(JNIUtil.getJNIEnv(), string, utf);
    }
}
