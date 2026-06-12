package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jlong (JNICALL *CallLongMethodA)(JNIEnv *env, jobject obj, jmethodID methodID, const jvalue *args);
public final class CallLongMethodA {

    private static final long code_base = JNINativeCall.create(CallLongMethodA.class, "()J", JNINativeCall.CALL_LONG_METHOD_A, 3);

    private static native long holder();

    private CallLongMethodA() {
    }

    public synchronized static long invoke(long JNIEnv, long obj, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, obj);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        return holder();
    }

    public synchronized static long invoke(long obj, long methodID, long args) {
        return invoke(JNIUtil.getJNIEnv(), obj, methodID, args);
    }
}
