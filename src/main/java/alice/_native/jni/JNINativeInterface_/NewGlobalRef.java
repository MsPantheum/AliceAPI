package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//    jobject (JNICALL *NewGlobalRef) (JNIEnv *env, jobject lobj);
public final class NewGlobalRef {

    private static final long code_base = JNINativeCall.create(NewGlobalRef.class, "()J", JNINativeCall.NEW_GLOBAL_REF, 1);

    private static native long holder();

    private NewGlobalRef() {
    }

    public synchronized static long invoke(long JNIEnv, long lobj) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, lobj);
        return holder();
    }

    public synchronized static long invoke(long lobj) {
        return invoke(JNIUtil.getJNIEnv(), lobj);
    }
}
