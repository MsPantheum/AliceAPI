package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ClearFieldAccessWatch) (jvmtiEnv* env, jclass klass, jfieldID field);
public final class ClearFieldAccessWatch {

    private ClearFieldAccessWatch() {
    }

    public static int invoke(long klass, long field) {
        return alice._native.jvmti.jvmtiInterface_1_.ClearFieldAccessWatch.invoke(JNIUtil.getJVMTIEnv(), klass, field);
    }
}
