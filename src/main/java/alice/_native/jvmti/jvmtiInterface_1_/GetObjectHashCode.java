package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetObjectHashCode) (jvmtiEnv* env, jobject object, jint* hash_code_ptr);
public final class GetObjectHashCode {

    private static final long code_base = JVMTINativeCall.create(GetObjectHashCode.class, "()I", JVMTINativeCall.GET_OBJECT_HASH_CODE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetObjectHashCode() {
    }

    public synchronized static int invoke(long JVMTIEnv, long object, long hash_code_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, object);
        JVMTINativeCall.setArg(code_base, 1, hash_code_ptr);
        return holder();
    }

    public synchronized static int invoke(long object, long hash_code_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), object, hash_code_ptr);
    }
}
