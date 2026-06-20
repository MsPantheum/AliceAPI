package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *AddToSystemClassLoaderSearch) (jvmtiEnv* env, const char* segment);
public final class AddToSystemClassLoaderSearch {

    private static final long code_base = JVMTINativeCall.create(AddToSystemClassLoaderSearch.class, "()I", JVMTINativeCall.ADD_TO_SYSTEM_CLASS_LOADER_SEARCH, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private AddToSystemClassLoaderSearch() {
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
