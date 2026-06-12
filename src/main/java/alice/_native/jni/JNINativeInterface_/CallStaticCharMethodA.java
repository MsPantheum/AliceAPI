package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jchar (JNICALL *CallStaticCharMethodA)(JNIEnv *env, jclass clazz, jmethodID methodID, const jvalue *args);
public final class CallStaticCharMethodA {

    private static final long code_base = JNINativeCall.create(CallStaticCharMethodA.class, "()C", JNINativeCall.CALL_STATIC_CHAR_METHOD_A, 3);

    private static native char holder();

    private CallStaticCharMethodA() {
    }

    public synchronized static char invoke(long JNIEnv, long clazz, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, clazz);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        return holder();
    }

    public synchronized static char invoke(long clazz, long methodID, long args) {
        return invoke(JNIUtil.getJNIEnv(), clazz, methodID, args);
    }
}
