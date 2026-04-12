package alice;

import alice.exception.BadEnvironment;
import alice.injector.classloading.Module2Reader;
import alice.util.*;

import javax.management.MXBean;
import java.lang.instrument.Instrumentation;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.net.Authenticator;
import java.nio.file.Files;
import java.util.Map;

public class Tset {
    public static void main(String[] args) throws Throwable {
        ModuleUtil.openAll();
        checkHSDB();
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        Map<ModuleReference, ModuleReader> m2r = ClassLoaderUtil.getModule2Reader(loader);
        Module2Reader neo = new Module2Reader(m2r);
        ClassLoaderUtil.setModule2Reader(loader, neo);
        Unsafe.ensureClassInitialized(Authenticator.class);
        Unsafe.ensureClassInitialized(MXBean.class);
        Unsafe.ensureClassInitialized(Instrumentation.class);
        Unsafe.ensureClassInitialized(Class.forName("test.Test"));
        System.out.println(neo);
        Nya.main(args);
    }

    private static void checkHSDB() {
        ClassLoader loader = ClassLoader.getSystemClassLoader();

        try {
            Class<?> app = Class.forName(Platform.jigsaw ? "jdk.internal.loader.ClassLoaders$AppClassLoader" : "sun.misc.Launcher$AppClassLoader");
            if (loader.getClass() != app) {
                throw new BadEnvironment("Modified system classloader! Current is " + loader.getClass().getName() + " loaded by " + loader.getClass().getClassLoader());
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            loader.loadClass("sun.jvm.hotspot.utilities.UnsupportedPlatformException");
        } catch (ClassNotFoundException e) {
            System.out.println("Append HSDB.");
            System.out.println("Path: " + FileUtil.getHSDB());
            if (!Files.exists(FileUtil.getHSDB())) {
                throw new RuntimeException("THE FUCK?");
            }
            ClassUtil.append(FileUtil.getHSDB(), loader);
        }

    }
}
