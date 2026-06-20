package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetJLocationFormat) (jvmtiEnv* env, jvmtiJlocationFormat* format_ptr);
public final class GetJLocationFormat {

    private static final long code_base = JVMTINativeCall.create(GetJLocationFormat.class, "()I", JVMTINativeCall.GET_J_LOCATION_FORMAT, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetJLocationFormat() {
    }

    public synchronized static int invoke(long JVMTIEnv, long format_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, format_ptr);
        return holder();
    }

    public synchronized static int invoke(long format_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), format_ptr);
    }
}
