package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *RetransformClasses) (jvmtiEnv* env, jint class_count, const jclass* classes);
public final class RetransformClasses {

    private RetransformClasses() {
    }

    public static int invoke(int class_count, long classes) {
        return alice._native.jvmti.jvmtiInterface_1_.RetransformClasses.invoke(JNIUtil.getJVMTIEnv(), class_count, classes);
    }
}
