package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetFieldModificationWatch) (jvmtiEnv* env, jclass klass, jfieldID field);
public final class SetFieldModificationWatch {

    private SetFieldModificationWatch() {
    }

    public static int invoke(long klass, long field) {
        return alice._native.jvmti.jvmtiInterface_1_.SetFieldModificationWatch.invoke(JNIUtil.getJVMTIEnv(), klass, field);
    }
}
