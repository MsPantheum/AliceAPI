package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jshort (JNICALL *CallStaticShortMethodA)(JNIEnv *env, jclass clazz, jmethodID methodID, const jvalue *args);
public final class CallStaticShortMethodA {

    private static final long code_base = JNINativeCall.create(CallStaticShortMethodA.class, "()S", JNINativeCall.CALL_STATIC_SHORT_METHOD_A, 3);

    private static native short holder();

    private CallStaticShortMethodA() {
    }

    public synchronized static short invoke(long JNIEnv, long clazz, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, clazz);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        return holder();
    }

    public synchronized static short invoke(long clazz, long methodID, long args) {
        return invoke(JNIUtil.getJNIEnv(), clazz, methodID, args);
    }
}
