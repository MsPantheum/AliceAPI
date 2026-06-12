package alice;

import alice._native.jni.JNINativeInterface_.CallStaticVoidMethodA;
import alice._native.jni.JNINativeInterface_.DeleteGlobalRef;
import alice._native.jni.JNINativeInterface_.FindClass;
import alice._native.jni.JNINativeInterface_.GetStaticMethodID;
import alice.util.AddressUtil;

public class Test2 {

    private static void test() {
        System.out.println("Called!");
    }

    public static void main(String[] args) {
        Init.ensureInit();
        long meow = AddressUtil.getObjAddress(Test2.class);
        System.out.println(Long.toHexString(meow));
        long address = FindClass.invoke("alice/Test2");
        try {
            long method = GetStaticMethodID.invoke(address, "test", "()V");
            System.out.println(Long.toHexString(method));
            CallStaticVoidMethodA.invoke(address, method, 0);
        } finally {
            DeleteGlobalRef.invoke(address);
        }
    }
}
