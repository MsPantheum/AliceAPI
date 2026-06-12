package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jfloat (JNICALL *CallFloatMethodA)(JNIEnv *env, jobject obj, jmethodID methodID, const jvalue *args);
public final class CallFloatMethodA {

    private static final long code_base = JNINativeCall.create(CallFloatMethodA.class, "()F", JNINativeCall.CALL_FLOAT_METHOD_A, 3);

    private static native float holder();

    private CallFloatMethodA() {
    }

    public synchronized static float invoke(long JNIEnv, long obj, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, obj);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        return holder();
    }

    public synchronized static float invoke(long obj, long methodID, long args) {
        return invoke(JNIUtil.getJNIEnv(), obj, methodID, args);
    }
}
