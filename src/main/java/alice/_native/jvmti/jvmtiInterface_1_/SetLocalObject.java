package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetLocalObject) (jvmtiEnv* env, jthread thread, jint depth, jint slot, jobject value);
public final class SetLocalObject {

    private static final long code_base = JVMTINativeCall.create(SetLocalObject.class, "()I", JVMTINativeCall.SET_LOCAL_OBJECT, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private SetLocalObject() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, int depth, int slot, long value) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, depth);
        JVMTINativeCall.setArg(code_base, 2, slot);
        JVMTINativeCall.setArg(code_base, 3, value);
        return holder();
    }

    public synchronized static int invoke(long thread, int depth, int slot, long value) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, depth, slot, value);
    }
}
