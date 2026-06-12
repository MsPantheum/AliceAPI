package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jchar (JNICALL *CallCharMethodA)(JNIEnv *env, jobject obj, jmethodID methodID, const jvalue *args);
public final class CallCharMethodA {

    private static final long code_base = JNINativeCall.create(CallCharMethodA.class, "()C", JNINativeCall.CALL_CHAR_METHOD_A, 3);

    private static native char holder();

    private CallCharMethodA() {
    }

    public synchronized static char invoke(long JNIEnv, long obj, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, obj);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        return holder();
    }

    public synchronized static char invoke(long obj, long methodID, long args) {
        return invoke(JNIUtil.getJNIEnv(), obj, methodID, args);
    }
}
