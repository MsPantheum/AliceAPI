package alice;

import alice._native.jvmti.jvmtiCapabilities;
import alice._native.jvmti.jvmtiInterface_1_.AddCapabilities;
import alice._native.jvmti.jvmtiInterface_1_.GetTag;
import alice._native.jvmti.jvmtiInterface_1_.SetTag;
import alice.exception.JNIException;
import alice.util.AddressUtil;
import alice.util.Unsafe;

public class Test10 {
    public static void main(String[] args) {
        Init.ensureInit();
        jvmtiCapabilities caps = new jvmtiCapabilities();
        try {
            caps.clear();
            caps.can_tag_objects(true);
            int ret = AddCapabilities.invoke(caps.address);
            if (ret != 0) {
                throw new JNIException("Failed to add JVMTI capabilities!", ret);
            }
            Object obj = new Object();
            System.out.println(obj);
            SetTag.invoke(AddressUtil.getObjAddress(obj), 0x114514L);
            long p = Unsafe.createPointer();
            GetTag.invoke(AddressUtil.getObjAddress(obj), p);
            System.out.println("0x".concat(Long.toHexString(Unsafe.getLong(p))));
        } finally {
            caps.release();
        }
    }
}
