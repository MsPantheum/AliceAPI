package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *NotifyFramePop) (jvmtiEnv* env, jthread thread, jint depth);
public final class NotifyFramePop {

    private NotifyFramePop() {
    }

    public static int invoke(long thread, int depth) {
        return alice._native.jvmti.jvmtiInterface_1_.NotifyFramePop.invoke(JNIUtil.getJVMTIEnv(), thread, depth);
    }
}
