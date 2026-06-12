package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jint (JNICALL *CallStaticIntMethodA)(JNIEnv *env, jclass clazz, jmethodID methodID, const jvalue *args);
public final class CallStaticIntMethodA {

    private static final long code_base = JNINativeCall.create(CallStaticIntMethodA.class, "()I", JNINativeCall.CALL_STATIC_INT_METHOD_A, 3);

    private static native int holder();

    private CallStaticIntMethodA() {
    }

    public synchronized static int invoke(long JNIEnv, long clazz, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, clazz);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        return holder();
    }

    public synchronized static int invoke(long clazz, long methodID, long args) {
        return invoke(JNIUtil.getJNIEnv(), clazz, methodID, args);
    }
}
