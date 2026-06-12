package alice.util;

import alice._native.jni.JNIInvokeInterface_.GetEnv;
import alice._native.jni.JNI_GetCreatedJavaVMs;
import alice.exception.JNIException;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import static alice.util.constants.Constants.JNI_OK;
import static alice.util.constants.Constants.JNI_VERSION_1_8;

public class JNIUtil {

    private static long JavaVM = -1;

    public static long getJavaVM() {
        if (JavaVM == -1) {
            JavaVM = getJavaVMs()[0];
        }
        return JavaVM;
    }

    public static long[] getJavaVMs() {
        long vmBuf = Unsafe.allocateMemory(Unsafe.ADDRESS_SIZE);
        int bufLen = Math.toIntExact(Unsafe.ADDRESS_SIZE);
        long nVMs = Unsafe.allocateMemory(Integer.BYTES);
        int ret = JNI_GetCreatedJavaVMs.invoke(vmBuf, bufLen, nVMs);
        if (ret != JNI_OK) {
            throw new JNIException("Failed to get JavaVMs!", ret);
        }
        int number = Unsafe.getInt(nVMs);
        long[] VMs = new long[number];
        Unsafe.freeMemory(nVMs);
        for (int i = 0; i < number; i++) {
            VMs[i] = Unsafe.getLong(vmBuf + (long) i * Unsafe.ADDRESS_SIZE);
        }
        Unsafe.freeMemory(vmBuf);
        return VMs;
    }

    private static final Object2LongMap<Thread> JNIEnvs = new Object2LongOpenHashMap<>();

    public static long getJNIEnv() {
        Thread currentThread = Thread.currentThread();
        if (JNIEnvs.containsKey(currentThread)) {
            return JNIEnvs.getLong(currentThread);
        }
        long penv = Unsafe.allocateMemory(Unsafe.ADDRESS_SIZE);
        int ret = GetEnv.invoke(JavaVM, penv, JNI_VERSION_1_8);//JNI_VERSION_1_8
        if (ret != JNI_OK) {
            throw new JNIException("Failed to get JNIEnv!", ret);
        }
        long JNIEnv = Unsafe.getLong(penv);
        JNIEnvs.put(currentThread, JNIEnv);
        Unsafe.freeMemory(penv);
        return JNIEnv;
    }
}
