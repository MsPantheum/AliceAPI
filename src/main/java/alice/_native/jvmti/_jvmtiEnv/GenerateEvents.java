package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GenerateEvents) (jvmtiEnv* env, jvmtiEvent event_type);
public final class GenerateEvents {

    private GenerateEvents() {
    }

    public static int invoke(int event_type) {
        return alice._native.jvmti.jvmtiInterface_1_.GenerateEvents.invoke(JNIUtil.getJVMTIEnv(), event_type);
    }
}
