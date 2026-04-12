package alice.util;

import alice.Platform;
import sun.misc.URLClassPath;

import java.net.URL;
import java.net.URLStreamHandler;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class UCPUtil {
    private static final long path_offset;
    private static final long unopenedUrls_offset;
    private static final long loaders_offset;
    private static final long lmap_offset;
    private static final long jarHandler_offset;
    private static final long JarLoader_handler_offset;

    private static final Class<?> JarLoader;

    static {
        Class<?> ucp = Platform.jigsaw ? jdk.internal.loader.URLClassPath.class : URLClassPath.class;
        try {
            JarLoader = Platform.jigsaw ? Class.forName("jdk.internal.loader.URLClassPath$JarLoader") : Class.forName("sun.misc.URLClassPath$JarLoader");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        path_offset = Unsafe.objectFieldOffset(ReflectionUtil.getField(ucp, "path"));
        loaders_offset = Unsafe.objectFieldOffset(ReflectionUtil.getField(ucp, "loaders"));
        lmap_offset = Unsafe.objectFieldOffset(ReflectionUtil.getField(ucp, "lmap"));
        jarHandler_offset = Unsafe.objectFieldOffset(ReflectionUtil.getField(ucp, "jarHandler"));
        JarLoader_handler_offset = Unsafe.objectFieldOffset(ReflectionUtil.getField(JarLoader, "handler"));
        if (Platform.jigsaw) {
            unopenedUrls_offset = Unsafe.objectFieldOffset(ReflectionUtil.getField(ucp, "unopenedUrls"));
        } else {
            unopenedUrls_offset = -1;
        }
    }

    public static ArrayList<URL> getPath(Object ucp) {
        return Unsafe.getObject(ucp, path_offset);
    }

    public static void setPath(Object ucp, ArrayList<URL> neo) {
        Unsafe.putObject(ucp, path_offset, neo);
    }

    public static ArrayList<?> getLoaders(Object ucp) {
        return Unsafe.getObject(ucp, loaders_offset);
    }

    public static void setLoaders(Object ucp, ArrayList<?> neo) {
        Unsafe.putObject(ucp, loaders_offset, neo);
    }

    public static HashMap<String, ?> getLmap(Object ucp) {
        return Unsafe.getObject(ucp, lmap_offset);
    }

    public static void setLmap(Object ucp, HashMap<String, ?> neo) {
        Unsafe.putObject(ucp, lmap_offset, neo);
    }

    public static URLStreamHandler getJarHandler(Object ucp) {
        return Unsafe.getObject(ucp, jarHandler_offset);
    }

    public static void setJarHandler(Object ucp, URLStreamHandler handler) {
        Unsafe.putObject(ucp, jarHandler_offset, handler);
    }

    public static ArrayDeque<URL> getUnopenedUrls(Object ucp) {
        return Unsafe.getObject(ucp, unopenedUrls_offset);
    }

    public static void setUnopenedUrls(Object ucp, ArrayDeque<URL> neo) {
        Unsafe.putObject(ucp, unopenedUrls_offset, neo);
    }

    public static URLStreamHandler getHandler(Object loader) {
        return Unsafe.getObject(loader, JarLoader_handler_offset);
    }

    public static void setHandler(Object loader, URLStreamHandler handler) {
        Unsafe.putObject(loader, JarLoader_handler_offset, handler);
    }

    public static boolean isJarLoader(Object o) {
        return JarLoader.isInstance(o);
    }
}
