package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetSourceFileName) (jvmtiEnv* env, jclass klass, char** source_name_ptr);
public final class GetSourceFileName {

    private static final long code_base = JVMTINativeCall.create(GetSourceFileName.class, "()I", JVMTINativeCall.GET_SOURCE_FILE_NAME, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetSourceFileName() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long source_name_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, source_name_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long source_name_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, source_name_ptr);
    }
}
