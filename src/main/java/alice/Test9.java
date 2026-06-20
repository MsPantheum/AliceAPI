package alice;

import alice._native.jvmti.jvmtiFrameInfo;
import alice._native.jvmti.jvmtiInterface_1_.Deallocate;
import alice._native.jvmti.jvmtiInterface_1_.GetAllStackTraces;
import alice._native.jvmti.jvmtiStackInfo;
import alice.exception.JNIException;
import alice.util.Converter;
import alice.util.Unsafe;
import sun.jvm.hotspot.oops.Metadata;
import sun.jvm.hotspot.oops.Method;

public class Test9 {
    public static void main(String[] args) {
        Init.ensureInit();
        long thread_count_ptr_p = Unsafe.allocateMemory(4);
        long jvmtiStackInfo_pp = Unsafe.allocateMemory(Unsafe.ADDRESS_SIZE);
        long stack_info = 0;
        try {
            Unsafe.putLong(jvmtiStackInfo_pp, 0);
            int ret = GetAllStackTraces.invoke(100, jvmtiStackInfo_pp, thread_count_ptr_p);
            if (ret != 0) {
                throw new JNIException("Failed to get all stack traces!", ret);
            }

            int thread_count = Unsafe.getInt(thread_count_ptr_p);
            stack_info = Unsafe.getLong(jvmtiStackInfo_pp);
            for (int threadIndex = 0; threadIndex < thread_count; threadIndex++) {
                jvmtiStackInfo info = new jvmtiStackInfo(stack_info + (long) threadIndex * jvmtiStackInfo.SIZE);
                if (info.frame_buffer() != 0) {
                    //System.out.println("0x".concat(Long.toHexString(info.frame_buffer())));
                    //System.out.println(info.frame_count());
                    for (int i = 0; i < info.frame_count(); i++) {
                        jvmtiFrameInfo frameInfo = new jvmtiFrameInfo(info.frame_buffer() + (long) i * jvmtiFrameInfo.SIZE);
                        long methodAddress = Unsafe.getLong(frameInfo.method());
                        if (methodAddress != 0) {
                            Method method = (Method) Metadata.instantiateWrapperFor(Converter.toAddress(methodAddress));
                            System.out.println(method.getName().asString().concat(method.getSignature().asString()));
                        }
                    }
                }
                System.out.println();
            }
        } finally {
            if (stack_info != 0) {
                int ret = Deallocate.invoke(stack_info);
                if (ret != 0) {
                    throw new JNIException("Failed to deallocate stack info!", ret);
                }
            }
            Unsafe.freeMemory(jvmtiStackInfo_pp);
            Unsafe.freeMemory(thread_count_ptr_p);
        }
    }
}
