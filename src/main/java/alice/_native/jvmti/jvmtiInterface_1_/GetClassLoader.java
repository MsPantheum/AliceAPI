package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetClassLoader) (jvmtiEnv* env, jclass klass, jobject* classloader_ptr);
public final class GetClassLoader {

    private static final long code_base = JVMTINativeCall.create(GetClassLoader.class, "()I", JVMTINativeCall.GET_CLASS_LOADER, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetClassLoader() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long classloader_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, classloader_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long classloader_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, classloader_ptr);
    }
}
