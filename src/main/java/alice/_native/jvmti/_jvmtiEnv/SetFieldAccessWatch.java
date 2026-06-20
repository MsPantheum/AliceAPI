package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *SetFieldAccessWatch) (jvmtiEnv* env, jclass klass, jfieldID field);
public final class SetFieldAccessWatch {

    private SetFieldAccessWatch() {
    }

    public static int invoke(long klass, long field) {
        return alice._native.jvmti.jvmtiInterface_1_.SetFieldAccessWatch.invoke(JNIUtil.getJVMTIEnv(), klass, field);
    }
}
