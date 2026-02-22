package alice;

import alice.exception.BadEnvironment;
import alice.injector.patch.PatcherLoader;
import alice.util.ClassUtil;
import alice.util.FileUtil;

import java.net.URLClassLoader;

public class Init {
    static void init() {
        URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        if(loader.getClass() != URLClassLoader.class){
            throw new BadEnvironment("Modified system classloader!");
        }
        try {
            loader.loadClass("sun.jvm.hotspot.utilities.UnsupportedPlatformException");
        } catch (ClassNotFoundException e) {
            ClassUtil.append(FileUtil.getHSDB(), loader);
        }
        PatcherLoader.load();
    }
}
