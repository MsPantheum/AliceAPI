package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//void (JNICALL *CallVoidMethodA)(JNIEnv *env, jobject obj, jmethodID methodID, const jvalue *args);
public final class CallVoidMethodA {

    private static final long code_base = JNINativeCall.create(CallVoidMethodA.class, "()V", JNINativeCall.CALL_VOID_METHOD_A, 3);

    private static native void holder();

    private CallVoidMethodA() {
    }

    public synchronized static void invoke(long JNIEnv, long obj, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, obj);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        holder();
    }

    public synchronized static void invoke(long obj, long methodID, long args) {
        invoke(JNIUtil.getJNIEnv(), obj, methodID, args);
    }
}
