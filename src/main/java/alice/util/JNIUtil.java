package alice.util;

import alice._native.jni.JNI_GetCreatedJavaVMs;
import alice._native.jni.JavaVM.GetEnv;
import alice.exception.JNIException;

import static alice.util.constants.Constants.JNI_OK;
import static alice.util.constants.Constants.JNI_VERSION_1_8;

public class JNIUtil {

    public static long getJavaVM() {
        return getJavaVMs()[0];
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

    public static long getJNIEnv(long JavaVM) {
        long penv = Unsafe.allocateMemory(Unsafe.ADDRESS_SIZE);
        int ret = GetEnv.invoke(JavaVM, penv, JNI_VERSION_1_8);//JNI_VERSION_1_8
        if (ret != JNI_OK) {
            throw new JNIException("Failed to get JNIEnv!", ret);
        }
        long JNIEnv = Unsafe.getLong(penv);
        Unsafe.freeMemory(penv);
        return JNIEnv;
    }
}
