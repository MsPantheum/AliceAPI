package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetClassVersionNumbers) (jvmtiEnv* env, jclass klass, jint* minor_version_ptr, jint* major_version_ptr);
public final class GetClassVersionNumbers {

    private static final long code_base = JVMTINativeCall.create(GetClassVersionNumbers.class, "()I", JVMTINativeCall.GET_CLASS_VERSION_NUMBERS, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetClassVersionNumbers() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long minor_version_ptr, long major_version_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, minor_version_ptr);
        JVMTINativeCall.setArg(code_base, 2, major_version_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long minor_version_ptr, long major_version_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, minor_version_ptr, major_version_ptr);
    }
}
