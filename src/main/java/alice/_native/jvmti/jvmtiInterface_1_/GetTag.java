package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetTag) (jvmtiEnv* env, jobject object, jlong* tag_ptr);
public final class GetTag {

    private static final long code_base = JVMTINativeCall.create(GetTag.class, "()I", JVMTINativeCall.GET_TAG, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetTag() {
    }

    public synchronized static int invoke(long JVMTIEnv, long object, long tag_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, object);
        JVMTINativeCall.setArg(code_base, 1, tag_ptr);
        return holder();
    }

    public synchronized static int invoke(long object, long tag_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), object, tag_ptr);
    }
}
