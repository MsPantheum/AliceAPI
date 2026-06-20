package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *FollowReferences) (jvmtiEnv* env, jint heap_filter, jclass klass, jobject initial_object, const jvmtiHeapCallbacks* callbacks, const void* user_data);
public final class FollowReferences {

    private FollowReferences() {
    }

    public static int invoke(int heap_filter, long klass, long initial_object, long callbacks, long user_data) {
        return alice._native.jvmti.jvmtiInterface_1_.FollowReferences.invoke(JNIUtil.getJVMTIEnv(), heap_filter, klass, initial_object, callbacks, user_data);
    }
}
