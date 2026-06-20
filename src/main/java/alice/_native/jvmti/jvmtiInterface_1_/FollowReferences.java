package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *FollowReferences) (jvmtiEnv* env, jint heap_filter, jclass klass, jobject initial_object, const jvmtiHeapCallbacks* callbacks, const void* user_data);
public final class FollowReferences {

    private static final long code_base = JVMTINativeCall.create(FollowReferences.class, "()I", JVMTINativeCall.FOLLOW_REFERENCES, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private FollowReferences() {
    }

    public synchronized static int invoke(long JVMTIEnv, int heap_filter, long klass, long initial_object, long callbacks, long user_data) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, heap_filter);
        JVMTINativeCall.setArg(code_base, 1, klass);
        JVMTINativeCall.setArg(code_base, 2, initial_object);
        JVMTINativeCall.setArg(code_base, 3, callbacks);
        JVMTINativeCall.setArg(code_base, 4, user_data);
        return holder();
    }

    public synchronized static int invoke(int heap_filter, long klass, long initial_object, long callbacks, long user_data) {
        return invoke(JNIUtil.getJVMTIEnv(), heap_filter, klass, initial_object, callbacks, user_data);
    }
}
