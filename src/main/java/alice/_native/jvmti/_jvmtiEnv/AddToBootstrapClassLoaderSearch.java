package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *AddToBootstrapClassLoaderSearch) (jvmtiEnv* env, const char* segment);
public final class AddToBootstrapClassLoaderSearch {

    private AddToBootstrapClassLoaderSearch() {
    }

    public static int invoke(long segment) {
        return alice._native.jvmti.jvmtiInterface_1_.AddToBootstrapClassLoaderSearch.invoke(JNIUtil.getJVMTIEnv(), segment);
    }
}
