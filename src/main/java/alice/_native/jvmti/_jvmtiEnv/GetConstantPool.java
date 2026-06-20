package alice._native.jvmti._jvmtiEnv;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetConstantPool) (jvmtiEnv* env, jclass klass, jint* constant_pool_count_ptr, jint* constant_pool_byte_count_ptr, unsigned char** constant_pool_bytes_ptr);
public final class GetConstantPool {

    private GetConstantPool() {
    }

    public static int invoke(long klass, long constant_pool_count_ptr, long constant_pool_byte_count_ptr, long constant_pool_bytes_ptr) {
        return alice._native.jvmti.jvmtiInterface_1_.GetConstantPool.invoke(JNIUtil.getJVMTIEnv(), klass, constant_pool_count_ptr, constant_pool_byte_count_ptr, constant_pool_bytes_ptr);
    }
}
