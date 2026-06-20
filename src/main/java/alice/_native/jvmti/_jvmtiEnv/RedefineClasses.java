package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *RedefineClasses) (jvmtiEnv* env, jint class_count, const jvmtiClassDefinition* class_definitions);
public final class RedefineClasses {

    private RedefineClasses() {
    }

    public static int invoke(int class_count, long class_definitions) {
        return alice._native.jvmti.jvmtiInterface_1_.RedefineClasses.invoke(JNIUtil.getJVMTIEnv(), class_count, class_definitions);
    }
}
