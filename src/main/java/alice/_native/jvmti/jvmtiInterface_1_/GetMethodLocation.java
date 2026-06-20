package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetMethodLocation) (jvmtiEnv* env, jmethodID method, jlocation* start_location_ptr, jlocation* end_location_ptr);
public final class GetMethodLocation {

    private static final long code_base = JVMTINativeCall.create(GetMethodLocation.class, "()I", JVMTINativeCall.GET_METHOD_LOCATION, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetMethodLocation() {
    }

    public synchronized static int invoke(long JVMTIEnv, long method, long start_location_ptr, long end_location_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, method);
        JVMTINativeCall.setArg(code_base, 1, start_location_ptr);
        JVMTINativeCall.setArg(code_base, 2, end_location_ptr);
        return holder();
    }

    public synchronized static int invoke(long method, long start_location_ptr, long end_location_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), method, start_location_ptr, end_location_ptr);
    }
}
