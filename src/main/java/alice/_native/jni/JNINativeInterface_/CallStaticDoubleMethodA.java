package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jdouble (JNICALL *CallStaticDoubleMethodA)(JNIEnv *env, jclass clazz, jmethodID methodID, const jvalue *args);
public final class CallStaticDoubleMethodA {

    private static final long code_base = JNINativeCall.create(CallStaticDoubleMethodA.class, "()D", JNINativeCall.CALL_STATIC_DOUBLE_METHOD_A, 3);

    private static native double holder();

    private CallStaticDoubleMethodA() {
    }

    public synchronized static double invoke(long JNIEnv, long clazz, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, clazz);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        return holder();
    }

    public synchronized static double invoke(long clazz, long methodID, long args) {
        return invoke(JNIUtil.getJNIEnv(), clazz, methodID, args);
    }
}
