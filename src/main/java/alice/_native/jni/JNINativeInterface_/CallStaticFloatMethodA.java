package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jfloat (JNICALL *CallStaticFloatMethodA)(JNIEnv *env, jclass clazz, jmethodID methodID, const jvalue *args);
public final class CallStaticFloatMethodA {

    private static final long code_base = JNINativeCall.create(CallStaticFloatMethodA.class, "()F", JNINativeCall.CALL_STATIC_FLOAT_METHOD_A, 3);

    private static native float holder();

    private CallStaticFloatMethodA() {
    }

    public synchronized static float invoke(long JNIEnv, long clazz, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, clazz);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        return holder();
    }

    public synchronized static float invoke(long clazz, long methodID, long args) {
        return invoke(JNIUtil.getJNIEnv(), clazz, methodID, args);
    }
}
