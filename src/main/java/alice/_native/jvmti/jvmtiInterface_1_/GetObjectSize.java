package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetObjectSize) (jvmtiEnv* env, jobject object, jlong* size_ptr);
public final class GetObjectSize {

    private static final long code_base = JVMTINativeCall.create(GetObjectSize.class, "()I", JVMTINativeCall.GET_OBJECT_SIZE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetObjectSize() {
    }

    public synchronized static int invoke(long JVMTIEnv, long object, long size_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, object);
        JVMTINativeCall.setArg(code_base, 1, size_ptr);
        return holder();
    }

    public synchronized static int invoke(long object, long size_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), object, size_ptr);
    }
}
