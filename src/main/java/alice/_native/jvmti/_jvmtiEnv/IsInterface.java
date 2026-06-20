package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *IsInterface) (jvmtiEnv* env, jclass klass, jboolean* is_interface_ptr);
public final class IsInterface {

    private IsInterface() {
    }

    public static int invoke(long klass, long is_interface_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.IsInterface.invoke(JNIUtil.getJVMTIEnv(), klass, is_interface_ptr);
    }
}
