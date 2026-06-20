package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetClassMethods) (jvmtiEnv* env, jclass klass, jint* method_count_ptr, jmethodID** methods_ptr);
public final class GetClassMethods {

    private GetClassMethods() {
    }

    public static int invoke(long klass, long method_count_ptr, long methods_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetClassMethods.invoke(JNIUtil.getJVMTIEnv(), klass, method_count_ptr, methods_ptr);
    }
}
