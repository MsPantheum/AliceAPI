package alice._native.jni.JNINativeInterface_;

import alice._native.CString;
import alice.util.JNIUtil;

//jstring (JNICALL *NewStringUTF)(JNIEnv *env, const char *bytes);
public final class NewStringUTF {

    private static final long code_base = JNINativeCall.create(NewStringUTF.class, "()J", JNINativeCall.NEW_STRING_UTF, 1);

    private static native long holder();

    private NewStringUTF() {
    }

    public synchronized static long invoke(long JNIEnv, long bytes) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, bytes);
        return holder();
    }

    public synchronized static long invoke(long JNIEnv, String bytes) {
        CString cbytes = CString.create(bytes);
        try {
            return invoke(JNIEnv, cbytes.address);
        } finally {
            cbytes.release();
        }
    }

    public synchronized static long invoke(String bytes) {
        return invoke(JNIUtil.getJNIEnv(), bytes);
    }
}
