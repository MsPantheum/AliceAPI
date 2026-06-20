package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetEventCallbacks) (jvmtiEnv* env, const jvmtiEventCallbacks* callbacks, jint size_of_callbacks);
public final class SetEventCallbacks {

    private static final long code_base = JVMTINativeCall.create(SetEventCallbacks.class, "()I", JVMTINativeCall.SET_EVENT_CALLBACKS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private SetEventCallbacks() {
    }

    public synchronized static int invoke(long JVMTIEnv, long callbacks, int size_of_callbacks) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, callbacks);
        JVMTINativeCall.setArg(code_base, 1, size_of_callbacks);
        return holder();
    }

    public synchronized static int invoke(long callbacks, int size_of_callbacks) {
        return invoke(JNIUtil.getJVMTIEnv(), callbacks, size_of_callbacks);
    }
}
