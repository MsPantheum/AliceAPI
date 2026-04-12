package alice.interceptor;

import alice.injector.classloading.UCPWrapper;
import alice.util.ClassLoaderUtil;
import sun.misc.URLClassPath;

import java.net.URLClassLoader;

public class URLClassLoaderInterceptor {
    public static URLClassLoader creation(URLClassLoader loader) {
        System.out.println("Processing classloader:" + loader.getClass().getName() + " from " + Thread.currentThread().getStackTrace()[2]);
        URLClassPath old = (URLClassPath) ClassLoaderUtil.getUCP(loader);
        ClassLoaderUtil.setUCP(loader, new UCPWrapper.LegacyURLClassPathWrapper(old));
        return loader;
    }
}
