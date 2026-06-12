package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jthrowable (JNICALL *ExceptionOccurred)(JNIEnv *env);
public final class ExceptionOccurred {

    private static final long code_base = JNINativeCall.create(ExceptionOccurred.class, "()J", JNINativeCall.EXCEPTION_OCCURRED, 0);

    private static native long holder();

    private ExceptionOccurred() {
    }

    public synchronized static long invoke(long JNIEnv) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        return holder();
    }

    public synchronized static long invoke() {
        return invoke(JNIUtil.getJNIEnv());
    }
}
