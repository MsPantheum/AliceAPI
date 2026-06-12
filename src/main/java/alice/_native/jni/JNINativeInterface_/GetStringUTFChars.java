package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//const char *(JNICALL *GetStringUTFChars)(JNIEnv *env, jstring string, jboolean *isCopy);
public final class GetStringUTFChars {

    private static final long code_base = JNINativeCall.create(GetStringUTFChars.class, "()J", JNINativeCall.GET_STRING_UTF_CHARS, 2);

    private static native long holder();

    private GetStringUTFChars() {
    }

    public synchronized static long invoke(long JNIEnv, long string, long isCopy) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, string);
        JNINativeCall.setArg(code_base, 1, isCopy);
        return holder();
    }

    public synchronized static long invoke(long string, long isCopy) {
        return invoke(JNIUtil.getJNIEnv(), string, isCopy);
    }
}
