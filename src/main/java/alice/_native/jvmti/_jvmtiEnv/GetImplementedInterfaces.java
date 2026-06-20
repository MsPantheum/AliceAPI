package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetImplementedInterfaces) (jvmtiEnv* env, jclass klass, jint* interface_count_ptr, jclass** interfaces_ptr);
public final class GetImplementedInterfaces {

    private GetImplementedInterfaces() {
    }

    public static int invoke(long klass, long interface_count_ptr, long interfaces_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetImplementedInterfaces.invoke(JNIUtil.getJVMTIEnv(), klass, interface_count_ptr, interfaces_ptr);
    }
}
