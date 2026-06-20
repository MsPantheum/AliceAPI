package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetAvailableProcessors) (jvmtiEnv* env, jint* processor_count_ptr);
public final class GetAvailableProcessors {

    private static final long code_base = JVMTINativeCall.create(GetAvailableProcessors.class, "()I", JVMTINativeCall.GET_AVAILABLE_PROCESSORS, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetAvailableProcessors() {
    }

    public synchronized static int invoke(long JVMTIEnv, long processor_count_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, processor_count_ptr);
        return holder();
    }

    public synchronized static int invoke(long processor_count_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), processor_count_ptr);
    }
}
