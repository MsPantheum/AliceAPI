package alice.interceptor;

import alice.util.ClassLoaderUtil;
import alice.util.URLClassPathWrapper;
import sun.misc.URLClassPath;

import java.net.URLClassLoader;

public class URLClassLoaderInterceptor {
    public static URLClassLoader creation(URLClassLoader loader) {
        URLClassPath old = ClassLoaderUtil.getUCP(loader);
        ClassLoaderUtil.setUCP(loader, new URLClassPathWrapper(old));
        return loader;
    }
}
