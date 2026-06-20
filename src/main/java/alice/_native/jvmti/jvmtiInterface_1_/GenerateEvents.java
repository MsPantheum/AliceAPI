package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GenerateEvents) (jvmtiEnv* env, jvmtiEvent event_type);
public final class GenerateEvents {

    private static final long code_base = JVMTINativeCall.create(GenerateEvents.class, "()I", JVMTINativeCall.GENERATE_EVENTS, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private GenerateEvents() {
    }

    public synchronized static int invoke(long JVMTIEnv, int event_type) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, event_type);
        return holder();
    }

    public synchronized static int invoke(int event_type) {
        return invoke(JNIUtil.getJVMTIEnv(), event_type);
    }
}
