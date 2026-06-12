package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jlong (JNICALL *CallStaticLongMethodA)(JNIEnv *env, jclass clazz, jmethodID methodID, const jvalue *args);
public final class CallStaticLongMethodA {

    private static final long code_base = JNINativeCall.create(CallStaticLongMethodA.class, "()J", JNINativeCall.CALL_STATIC_LONG_METHOD_A, 3);

    private static native long holder();

    private CallStaticLongMethodA() {
    }

    public synchronized static long invoke(long JNIEnv, long clazz, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, clazz);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        return holder();
    }

    public synchronized static long invoke(long clazz, long methodID, long args) {
        return invoke(JNIUtil.getJNIEnv(), clazz, methodID, args);
    }
}
