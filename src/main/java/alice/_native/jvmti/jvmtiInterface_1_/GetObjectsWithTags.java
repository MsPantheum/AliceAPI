package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetObjectsWithTags) (jvmtiEnv* env, jint tag_count, const jlong* tags, jint* count_ptr, jobject** object_result_ptr, jlong** tag_result_ptr);
public final class GetObjectsWithTags {

    private static final long code_base = JVMTINativeCall.create(GetObjectsWithTags.class, "()I", JVMTINativeCall.GET_OBJECTS_WITH_TAGS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetObjectsWithTags() {
    }

    public synchronized static int invoke(long JVMTIEnv, int tag_count, long tags, long count_ptr, long object_result_ptr, long tag_result_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, tag_count);
        JVMTINativeCall.setArg(code_base, 1, tags);
        JVMTINativeCall.setArg(code_base, 2, count_ptr);
        JVMTINativeCall.setArg(code_base, 3, object_result_ptr);
        JVMTINativeCall.setArg(code_base, 4, tag_result_ptr);
        return holder();
    }

    public synchronized static int invoke(int tag_count, long tags, long count_ptr, long object_result_ptr, long tag_result_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), tag_count, tags, count_ptr, object_result_ptr, tag_result_ptr);
    }
}
