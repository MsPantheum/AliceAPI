package alice._native.jvmti.jvmtiInterface_1_;

import alice.util.JNIUtil;

//jvmtiError (JNICALL *Deallocate) (jvmtiEnv* env, unsigned char* mem);
public final class Deallocate {

    private static final long code_base = JVMTINativeCall.create(Deallocate.class, "()I", JVMTINativeCall.DEALLOCATE, new int[]{JVMTINativeCall.INTEGER});

    private static native int holder();

    private Deallocate() {
    }

    public synchronized static int invoke(long JVMTIEnv, long mem) {
        JVMTINativeCall.setEnv(code_base, JVMTIEnv);
        JVMTINativeCall.setArg(code_base, 0, mem);
        return holder();
    }

    public synchronized static int invoke(long mem) {
        return invoke(JNIUtil.getJVMTIEnv(), mem);
    }
}
