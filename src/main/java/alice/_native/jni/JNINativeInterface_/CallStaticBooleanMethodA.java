package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jboolean (JNICALL *CallStaticBooleanMethodA)(JNIEnv *env, jclass clazz, jmethodID methodID, const jvalue *args);
public final class CallStaticBooleanMethodA {

    private static final long code_base = JNINativeCall.create(CallStaticBooleanMethodA.class, "()Z", JNINativeCall.CALL_STATIC_BOOLEAN_METHOD_A, 3);

    private static native boolean holder();

    private CallStaticBooleanMethodA() {
    }

    public synchronized static boolean invoke(long JNIEnv, long clazz, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, clazz);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        return holder();
    }

    public synchronized static boolean invoke(long clazz, long methodID, long args) {
        return invoke(JNIUtil.getJNIEnv(), clazz, methodID, args);
    }
}
