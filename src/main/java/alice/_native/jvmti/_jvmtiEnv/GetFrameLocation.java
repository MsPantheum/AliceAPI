package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetFrameLocation) (jvmtiEnv* env, jthread thread, jint depth, jmethodID* method_ptr, jlocation* location_ptr);
public final class GetFrameLocation {

    private GetFrameLocation() {
    }

    public static int invoke(long thread, int depth, long method_ptr, long location_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetFrameLocation.invoke(JNIUtil.getJVMTIEnv(), thread, depth, method_ptr, location_ptr);
    }
}
