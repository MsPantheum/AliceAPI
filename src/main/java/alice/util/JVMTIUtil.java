package alice.util;

import alice.Platform;
import alice._native.jni.JNINativeInterface_.DeleteGlobalRef;
import alice._native.jni.JNINativeInterface_.FindClass;
import alice._native.jni.JNINativeInterface_.GetMethodID;
import alice._native.jvmti.JPLISAgent;

public class JVMTIUtil {

    private static final long mTransform;

    static {
        long clazz = FindClass.invoke("sun/instrument/InstrumentationImpl");
        try {
            mTransform = GetMethodID.invoke(clazz, "transform", Platform.jigsaw ? "(Ljava/lang/Module;Ljava/lang/ClassLoader;Ljava/lang/String;Ljava/lang/Class;Ljava/security/ProtectionDomain;[BZ)[B" : "(Ljava/lang/ClassLoader;Ljava/lang/String;Ljava/lang/Class;Ljava/security/ProtectionDomain;[BZ)[B");
        } finally {
            DeleteGlobalRef.invoke(clazz);
        }
    }

    public static JPLISAgent setupJPLISAgent() {
        long jvmti = JNIUtil.getJVMTIEnv();
        JPLISAgent agent = new JPLISAgent();
        agent.mJVM(JNIUtil.getJavaVM());
        agent.mNativeMethodPrefixAvailable(true);
        agent.mRedefineAvailable(true);
        JPLISAgent.JPLISEnvironment normal = agent.mNormalEnvironment();
        normal.mAgent(agent.address);
        normal.mJVMTIEnv(jvmti);
        normal.mIsRetransformer(false);
        JPLISAgent.JPLISEnvironment retransform = agent.mRetransformEnvironment();
        retransform.mJVMTIEnv(jvmti);
        retransform.mAgent(agent.address);
        retransform.mIsRetransformer(true);
        return agent;
    }
}
