package alice.interceptor;

import alice.Platform;
import alice.injector.classloading.UCPWrapper;
import alice.log.Logger;
import alice.util.ClassLoaderUtil;
import sun.misc.URLClassPath;

import java.net.URLClassLoader;

import static alice.injector.ClassPatcher.replaceJarHandler;
import static alice.injector.ClassPatcher.replaceLoaders;

public class URLClassLoaderInterceptor {
    public static URLClassLoader creation(URLClassLoader loader) {
        Logger.MAIN.info("Processing classloader:" + loader.getClass().getName() + " from " + Thread.currentThread().getStackTrace()[2]);
        if (!Platform.jigsaw) {
            URLClassPath old = (URLClassPath) ClassLoaderUtil.getUCP(loader);
            ClassLoaderUtil.setUCP(loader, new UCPWrapper.LegacyURLClassPathWrapper(old));
        } else {
            Object ucp = ClassLoaderUtil.getUCP(loader);
            replaceLoaders(ucp);
            replaceJarHandler(ucp);
        }
        return loader;
    }
}
