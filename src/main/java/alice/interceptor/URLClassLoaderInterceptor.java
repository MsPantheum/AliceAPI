package alice.interceptor;

import alice.log.Logger;
import alice.util.ClassLoaderUtil;

import java.net.URLClassLoader;

import static alice.injector.ClassPatcher.replaceJarHandler;
import static alice.injector.ClassPatcher.replaceLoaders;

public final class URLClassLoaderInterceptor {
    public static URLClassLoader creation(URLClassLoader loader) {
        Logger.MAIN.info("Processing classloader:" + loader.getClass().getName() + " from " + Thread.currentThread().getStackTrace()[2]);
        Object ucp = ClassLoaderUtil.getUCP(loader);
        replaceLoaders(ucp);
        replaceJarHandler(ucp);
        return loader;
    }
}
