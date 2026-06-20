package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *ClearFieldModificationWatch) (jvmtiEnv* env, jclass klass, jfieldID field);
public final class ClearFieldModificationWatch {

    private ClearFieldModificationWatch() {
    }

    public static int invoke(long klass, long field) {
        return alice._native.jvmti.jvmtiInterface_1_.ClearFieldModificationWatch.invoke(JNIUtil.getJVMTIEnv(), klass, field);
    }
}
