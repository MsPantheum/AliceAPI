package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jint (JNICALL *CallIntMethodA)(JNIEnv *env, jobject obj, jmethodID methodID, const jvalue *args);
public final class CallIntMethodA {

    private static final long code_base = JNINativeCall.create(CallIntMethodA.class, "()I", JNINativeCall.CALL_INT_METHOD_A, 3);

    private static native int holder();

    private CallIntMethodA() {
    }

    public synchronized static int invoke(long JNIEnv, long obj, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, obj);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        return holder();
    }

    public synchronized static int invoke(long obj, long methodID, long args) {
        return invoke(JNIUtil.getJNIEnv(), obj, methodID, args);
    }

}
