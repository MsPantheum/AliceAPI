package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *Allocate) (jvmtiEnv* env, jlong size, unsigned char** mem_ptr);
public final class Allocate {

    private static final long code_base = JVMTINativeCall.create(Allocate.class, "()I", JVMTINativeCall.ALLOCATE, new int[]{JVMTINativeCall.INTEGER, JVMTINativeCall.INTEGER});

    private static native int holder();

    private Allocate() {
    }

    public synchronized static int invoke(long JVMTIEnv, long size, long mem_ptr) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, size);
        JVMTINativeCall.setArg(code_base, 1, mem_ptr);
        return holder();
    }

    public synchronized static int invoke(long size, long mem_ptr) {
        return invoke(JNIUtil.getJVMTIEnv(), size, mem_ptr);
    }
}
