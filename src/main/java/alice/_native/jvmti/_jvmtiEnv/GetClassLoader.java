package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetClassLoader) (jvmtiEnv* env, jclass klass, jobject* classloader_ptr);
public final class GetClassLoader {

    private GetClassLoader() {
    }

    public static int invoke(long klass, long classloader_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetClassLoader.invoke(JNIUtil.getJVMTIEnv(), klass, classloader_ptr);
    }
}
