package alice.interceptor;

import alice.util.ClassLoaderUtil;
import alice.util.URLClassPathWrapper;
import sun.misc.URLClassPath;

import java.net.URLClassLoader;

public class URLClassLoaderInterceptor {
    public static URLClassLoader creation(URLClassLoader loader) {
        System.out.println("Processing classloader:" + loader.getClass().getName() + " from " + Thread.currentThread().getStackTrace()[2]);
        URLClassPath old = ClassLoaderUtil.getUCP(loader);
        ClassLoaderUtil.setUCP(loader, new URLClassPathWrapper(old));
        return loader;
    }
}
