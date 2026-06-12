package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jbyte (JNICALL *CallByteMethodA)(JNIEnv *env, jobject obj, jmethodID methodID, const jvalue *args);
public final class CallByteMethodA {

    private static final long code_base = JNINativeCall.create(CallByteMethodA.class, "()B", JNINativeCall.CALL_BYTE_METHOD_A, 3);

    private static native byte holder();

    private CallByteMethodA() {
    }

    public synchronized static byte invoke(long JNIEnv, long obj, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, obj);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        return holder();
    }

    public synchronized static byte invoke(long obj, long methodID, long args) {
        return invoke(JNIUtil.getJNIEnv(), obj, methodID, args);
    }
}
