package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *Allocate) (jvmtiEnv* env, jlong size, unsigned char** mem_ptr);
public final class Allocate {

    private Allocate() {
    }

    public static int invoke(long size, long mem_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.Allocate.invoke(JNIUtil.getJVMTIEnv(), size, mem_ptr);
    }
}
