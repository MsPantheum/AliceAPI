package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetJLocationFormat) (jvmtiEnv* env, jvmtiJlocationFormat* format_ptr);
public final class GetJLocationFormat {

    private GetJLocationFormat() {
    }

    public static int invoke(long format_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetJLocationFormat.invoke(JNIUtil.getJVMTIEnv(), format_ptr);
    }
}
