package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetMethodLocation) (jvmtiEnv* env, jmethodID method, jlocation* start_location_ptr, jlocation* end_location_ptr);
public final class GetMethodLocation {

    private GetMethodLocation() {
    }

    public static int invoke(long method, long start_location_ptr, long end_location_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetMethodLocation.invoke(JNIUtil.getJVMTIEnv(), method, start_location_ptr, end_location_ptr);
    }
}
