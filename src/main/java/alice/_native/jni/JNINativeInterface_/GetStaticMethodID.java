package alice._native.jni.JNINativeInterface_;

import alice._native.CString;
import alice.util.JNIUtil;

//jmethodID (JNICALL *GetStaticMethodID)(JNIEnv *env, jclass clazz, const char *name, const char *sig);
public final class GetStaticMethodID {

    private static final long code_base = JNINativeCall.create(GetStaticMethodID.class, "()J", JNINativeCall.GET_STATIC_METHOD_ID, 3);

    private static native long holder();

    private GetStaticMethodID() {
    }

    public synchronized static long invoke(long JNIEnv, long clazz, long name, long sig) {
        JNINativeCall.setEnv(code_base, JNIEnv);
        JNINativeCall.setArg(code_base, 0, clazz);
        JNINativeCall.setArg(code_base, 1, name);
        JNINativeCall.setArg(code_base, 2, sig);
        return holder();
    }

    public synchronized static long invoke(long JNIEnv, long clazz, String name, String sig) {
        CString cname = CString.create(name);
        CString csig = CString.create(sig);
        try {
            return invoke(JNIEnv, clazz, cname.address, csig.address);
        } finally {
            csig.release();
            cname.release();
        }
    }

    public synchronized static long invoke(long clazz, String name, String sig) {
        return invoke(JNIUtil.getJNIEnv(), clazz, name, sig);
    }
}
