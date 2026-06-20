package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetFrameLocation) (jvmtiEnv* env, jthread thread, jint depth, jmethodID* method_ptr, jlocation* location_ptr);
public final class GetFrameLocation {

    private static final long code_base = JVMTINativeCall.create(GetFrameLocation.class, "()I", JVMTINativeCall.GET_FRAME_LOCATION, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetFrameLocation() {
    }

    public synchronized static int invoke(long JVMTIEnv, long thread, int depth, long method_ptr, long location_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, thread);
        JVMTINativeCall.setArg(code_base, 1, depth);
        JVMTINativeCall.setArg(code_base, 2, method_ptr);
        JVMTINativeCall.setArg(code_base, 3, location_ptr);
        return holder();
    }

    public synchronized static int invoke(long thread, int depth, long method_ptr, long location_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), thread, depth, method_ptr, location_ptr);
    }
}
