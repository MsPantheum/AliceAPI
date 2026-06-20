package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetClassVersionNumbers) (jvmtiEnv* env, jclass klass, jint* minor_version_ptr, jint* major_version_ptr);
public final class GetClassVersionNumbers {

    private GetClassVersionNumbers() {
    }

    public static int invoke(long klass, long minor_version_ptr, long major_version_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetClassVersionNumbers.invoke(JNIUtil.getJVMTIEnv(), klass, minor_version_ptr, major_version_ptr);
    }
}
