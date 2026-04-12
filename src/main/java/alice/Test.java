package alice;

import alice.api.ClassByteProcessor;
import alice.injector.ClassPatcher;
import alice.util.*;
import jdk.internal.loader.URLClassPath;
import org.objectweb.asm.Opcodes;
import sun.net.www.protocol.jar.Handler;

import java.net.URLStreamHandler;

public class Test implements Opcodes {

    private static void test() {
        DebugUtil.printStackTrace();
    }

    public static void main(String[] args) throws Throwable {
        ModuleUtil.openAll();
        URLClassPath ucp = (URLClassPath) ClassLoaderUtil.getUCP(ClassLoader.getSystemClassLoader());
        URLStreamHandler handler = UCPUtil.getJarHandler(ucp);
        if (handler == null) {
            System.out.println("Create neo.");
            handler = new Handler();
        }
        if (handler instanceof Handler) {
            handler = new ClassPatcher.StreamPatcher((Handler) handler);
            UCPUtil.setJarHandler(ucp, handler);
        } else {
            throw new IllegalStateException();
        }
        for (Object o : UCPUtil.getLoaders(ucp)) {
            if (UCPUtil.isJarLoader(o)) {
                System.out.println("Set:" + o);
                UCPUtil.setHandler(o, handler);
            }
        }
        Unsafe.ensureClassInitialized(YSQD.class);
        Unsafe.ensureClassInitialized(ClassByteProcessor.class);
        Unsafe.ensureClassInitialized(Class.forName("test.Test"));
        System.out.println("-----------------------------------------------------");
        System.out.println("unopenedUrls");
        System.out.println(UCPUtil.getUnopenedUrls(ucp));
        System.out.println("path");
        System.out.println(UCPUtil.getPath(ucp));
        System.out.println("lmap");
        System.out.println(UCPUtil.getLmap(ucp));
        System.out.println("loaders");
        System.out.println(UCPUtil.getLoaders(ucp));
    }

}
