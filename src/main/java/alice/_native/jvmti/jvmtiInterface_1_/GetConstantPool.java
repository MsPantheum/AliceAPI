package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *GetConstantPool) (jvmtiEnv* env, jclass klass, jint* constant_pool_count_ptr, jint* constant_pool_byte_count_ptr, unsigned char** constant_pool_bytes_ptr);
public final class GetConstantPool {

    private static final long code_base = JVMTINativeCall.create(GetConstantPool.class, "()I", JVMTINativeCall.GET_CONSTANT_POOL, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private GetConstantPool() {
    }

    public synchronized static int invoke(long JVMTIEnv, long klass, long constant_pool_count_ptr, long constant_pool_byte_count_ptr, long constant_pool_bytes_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, klass);
        JVMTINativeCall.setArg(code_base, 1, constant_pool_count_ptr);
        JVMTINativeCall.setArg(code_base, 2, constant_pool_byte_count_ptr);
        JVMTINativeCall.setArg(code_base, 3, constant_pool_bytes_ptr);
        return holder();
    }

    public synchronized static int invoke(long klass, long constant_pool_count_ptr, long constant_pool_byte_count_ptr, long constant_pool_bytes_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), klass, constant_pool_count_ptr, constant_pool_byte_count_ptr, constant_pool_bytes_ptr);
    }
}
