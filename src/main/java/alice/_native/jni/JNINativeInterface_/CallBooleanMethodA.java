package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jboolean (JNICALL *CallBooleanMethodA)(JNIEnv *env, jobject obj, jmethodID methodID, const jvalue *args);
public final class CallBooleanMethodA {

    private static final long code_base = JNINativeCall.create(CallBooleanMethodA.class, "()Z", JNINativeCall.CALL_BOOLEAN_METHOD_A, 3);

    private static native boolean holder();

    private CallBooleanMethodA() {
    }

    public synchronized static boolean invoke(long JNIEnv, long obj, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, obj);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        return holder();
    }

    public synchronized static boolean invoke(long obj, long methodID, long args) {
        return invoke(JNIUtil.getJNIEnv(), obj, methodID, args);
    }
}
