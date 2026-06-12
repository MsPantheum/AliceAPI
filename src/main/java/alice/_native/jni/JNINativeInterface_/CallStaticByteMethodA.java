package alice._native.jni.JNINativeInterface_;

import alice.util.JNIUtil;

//jbyte (JNICALL *CallStaticByteMethodA)(JNIEnv *env, jclass clazz, jmethodID methodID, const jvalue *args);
public final class CallStaticByteMethodA {

    private static final long code_base = JNINativeCall.create(CallStaticByteMethodA.class, "()B", JNINativeCall.CALL_STATIC_BYTE_METHOD_A, 3);

    private static native byte holder();

    private CallStaticByteMethodA() {
    }

    public synchronized static byte invoke(long JNIEnv, long clazz, long methodID, long args) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, clazz);
        JNINativeCall.setArg(code_base, 1, methodID);
        JNINativeCall.setArg(code_base, 2, args);
        return holder();
    }

    public synchronized static byte invoke(long clazz, long methodID, long args) {
        return invoke(JNIUtil.getJNIEnv(), clazz, methodID, args);
    }
}
