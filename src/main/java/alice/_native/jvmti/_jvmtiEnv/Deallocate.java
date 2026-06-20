package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *Deallocate) (jvmtiEnv* env, unsigned char* mem);
public final class Deallocate {

    private Deallocate() {
    }

    public static int invoke(long mem) {
        return alice._native.jvmti.jvmtiInterface_1_.Deallocate.invoke(JNIUtil.getJVMTIEnv(), mem);
    }
}
