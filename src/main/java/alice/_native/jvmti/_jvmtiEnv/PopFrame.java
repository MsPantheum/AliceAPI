package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *PopFrame) (jvmtiEnv* env, jthread thread);
public final class PopFrame {

    private PopFrame() {
    }

    public static int invoke(long thread) {
        return alice._native.jvmti.jvmtiInterface_1_.PopFrame.invoke(JNIUtil.getJVMTIEnv(), thread);
    }
}
