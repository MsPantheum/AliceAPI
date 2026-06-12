package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jclass (JNICALL *GetObjectClass)(JNIEnv *env, jobject obj);
public final class GetObjectClass {

    private static final long code_base = JNINativeCall.create(GetObjectClass.class, "()J", JNINativeCall.GET_OBJECT_CLASS, 1);

    private static native long holder();

    private GetObjectClass() {
    }

    public synchronized static long invoke(long JNIEnv, long obj) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, obj);
        return holder();
    }

    public synchronized static long invoke(long obj) {
        return invoke(JNIUtil.getJNIEnv(), obj);
    }
}
