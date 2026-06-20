package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *AddToSystemClassLoaderSearch) (jvmtiEnv* env, const char* segment);
public final class AddToSystemClassLoaderSearch {

    private AddToSystemClassLoaderSearch() {
    }

    public static int invoke(long segment) {
        return alice._native.jvmti.jvmtiInterface_1_.AddToSystemClassLoaderSearch.invoke(JNIUtil.getJVMTIEnv(), segment);
    }
}
