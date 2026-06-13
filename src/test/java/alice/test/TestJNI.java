package alice.test;

import alice.Init;
import alice._native.jni.JNINativeInterface_.CallStaticLongMethodA;
import alice._native.jni.JNINativeInterface_.DeleteGlobalRef;
import alice._native.jni.JNINativeInterface_.FindClass;
import alice._native.jni.JNINativeInterface_.GetStaticMethodID;
import org.junit.jupiter.api.Test;

public class TestJNI {

    @SuppressWarnings("unused")
    private static long method() {
        return 114514191980L;
    }

    @Test
    public void test() {
        Init.ensureInit();
        long class_p = FindClass.invoke("alice/test/TestJNI");
        long result;
        try {
            long method = GetStaticMethodID.invoke(class_p, "method", "()J");
            result = CallStaticLongMethodA.invoke(class_p, method, 0);
        } finally {
            DeleteGlobalRef.invoke(class_p);
        }
        assert result == 114514191980L;
    }
}
