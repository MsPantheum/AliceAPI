package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *AddToBootstrapClassLoaderSearch) (jvmtiEnv* env, const char* segment);
public final class AddToBootstrapClassLoaderSearch {

    private static final long code_base = JVMTINativeCall.create(AddToBootstrapClassLoaderSearch.class, "()I", JVMTINativeCall.ADD_TO_BOOTSTRAP_CLASS_LOADER_SEARCH, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private AddToBootstrapClassLoaderSearch() {
    }

    public synchronized static int invoke(long JVMTIEnv, long segment) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, segment);
        return holder();
    }

    public synchronized static int invoke(long segment) {
        return invoke(JNIUtil.getJVMTIEnv(), segment);
    }
}
