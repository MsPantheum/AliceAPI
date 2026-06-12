package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//void (JNICALL *CallStaticVoidMethodA)(JNIEnv *env, jclass clazz, jmethodID methodID, const jvalue *args);
public final class CallStaticVoidMethodA {

    private static final long code_base = JNINativeCall.create(CallStaticVoidMethodA.class, "()V", JNINativeCall.CALL_STATIC_VOID_METHOD_A, 3);

    private static native void holder();

    private CallStaticVoidMethodA() {
    }

    public synchronized static void invoke(long JNIEnv, long clazz, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, clazz);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        holder();
    }

    public synchronized static void invoke(long clazz, long methodID, long args) {
        invoke(JNIUtil.getJNIEnv(), clazz, methodID, args);
    }
}
