package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetObjectsWithTags) (jvmtiEnv* env, jint tag_count, const jlong* tags, jint* count_ptr, jobject** object_result_ptr, jlong** tag_result_ptr);
public final class GetObjectsWithTags {

    private GetObjectsWithTags() {
    }

    public static int invoke(int tag_count, long tags, long count_ptr, long object_result_ptr, long tag_result_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetObjectsWithTags.invoke(JNIUtil.getJVMTIEnv(), tag_count, tags, count_ptr, object_result_ptr, tag_result_ptr);
    }
}
