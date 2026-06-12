package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jshort (JNICALL *CallShortMethodA)(JNIEnv *env, jobject obj, jmethodID methodID, const jvalue *args);
public final class CallShortMethodA {

    private static final long code_base = JNINativeCall.create(CallShortMethodA.class, "()S", JNINativeCall.CALL_SHORT_METHOD_A, 3);

    private static native short holder();

    private CallShortMethodA() {
    }

    public synchronized static short invoke(long JNIEnv, long obj, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, obj);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        return holder();
    }

    public synchronized static short invoke(long obj, long methodID, long args) {
        return invoke(JNIUtil.getJNIEnv(), obj, methodID, args);
    }
}
