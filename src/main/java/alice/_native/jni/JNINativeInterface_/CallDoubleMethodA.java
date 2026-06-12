package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jdouble (JNICALL *CallDoubleMethodA)(JNIEnv *env, jobject obj, jmethodID methodID, const jvalue *args);
public final class CallDoubleMethodA {

    private static final long code_base = JNINativeCall.create(CallDoubleMethodA.class, "()D", JNINativeCall.CALL_DOUBLE_METHOD_A, 3);

    private static native double holder();

    private CallDoubleMethodA() {
    }

    public synchronized static double invoke(long JNIEnv, long obj, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, obj);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        return holder();
    }

    public synchronized static double invoke(long obj, long methodID, long args) {
        return invoke(JNIUtil.getJNIEnv(), obj, methodID, args);
    }
}
