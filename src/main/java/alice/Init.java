package alice;

import alice.exception.BadEnvironment;
import alice.injector.patch.PatcherLoader;
import alice.util.ClassUtil;
import alice.util.FileUtil;
import alice.util.Unsafe;

import java.net.URLClassLoader;

public class Init {

    private static void checkHSDB(){
        URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();

        try {
            Class<?> app = loader.loadClass("sun.misc.Launcher$AppClassLoader");
            if (loader.getClass() != app) {
                throw new BadEnvironment("Modified system classloader! Current is " + loader.getClass().getName() + " loaded by " + loader.getClass().getClassLoader());
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            loader.loadClass("sun.jvm.hotspot.utilities.UnsupportedPlatformException");
        } catch (ClassNotFoundException e) {
            ClassUtil.append(FileUtil.getHSDB(), loader);
        }
    }

    private static boolean init = false;

    static void init() {
        checkHSDB();
        Unsafe.ensureClassInitialized(Platform.class);
        PatcherLoader.load();
        Unsafe.ensureClassInitialized(HSDB.class);
        init = true;
    }

    public static void ensureInit() {
        if(!init){
            init();
        }
    }
}
