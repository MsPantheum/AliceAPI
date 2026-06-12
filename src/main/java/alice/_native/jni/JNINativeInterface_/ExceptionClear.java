package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//void (JNICALL *ExceptionClear)(JNIEnv *env);
public final class ExceptionClear {

    private static final long code_base = JNINativeCall.create(ExceptionClear.class, "()V", JNINativeCall.EXCEPTION_CLEAR, 0);

    private static native void holder();

    private ExceptionClear() {
    }

    public synchronized static void invoke(long JNIEnv) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        holder();
    }

    public synchronized static void invoke() {
        invoke(JNIUtil.getJNIEnv());
    }
}
