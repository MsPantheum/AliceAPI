package alice;

import alice.exception.BadEnvironment;
import alice.injector.patch.PatcherLoader;
import alice.util.ClassUtil;
import alice.util.FileUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.utilities.PlatformInfo;

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

    private static void checkPlatform(){
        String cpu = PlatformInfo.getCPU();
        String os = PlatformInfo.getOS();
        if(!(cpu.equals("amd64") || cpu.equals("x86_64"))){
            throw new BadEnvironment("CPU arch is not supported!");
        }
        if(os.equals("darwin")){
            System.err.println("Darwin hasn't been tested! And it will never be tested and officially supported unless someone buy me a Mac.");
        }
        if(os.equals("bsd")){
            System.err.println("Bsd should be supported but it's not guaranteed.");
        }
        if(os.equals("solaris")){
            System.err.println("???? We are running on solaris!");
        }
        if(os.equals("win32")) {
            System.out.println("Running on windows!");
            System.setProperty("sun.jvm.hotspot.debugger.windbg.disableNativeLookup","true");
        }
    }

    static void init() {
        checkHSDB();
        checkPlatform();
        PatcherLoader.load();
        Unsafe.ensureClassInitialized(HSDB.class);
    }
}
