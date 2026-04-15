package alice.util;

import alice.Platform;
import jdk.internal.loader.URLClassPath;

import java.lang.invoke.VarHandle;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class UCPUtil {
    private static final long path_offset;
    private static final VarHandle path_handle;
    private static final VarHandle unopenedUrls_handle;
    private static final long loaders_offset;
    private static final VarHandle loaders_handle;
    private static final long lmap_offset;
    private static final VarHandle lmap_handle;
    private static final long jarHandler_offset;
    private static final VarHandle jarHandler_handle;

    private static final Class<?> JarLoader;

    static {
        try {
            Class<?> ucp;
            if (Platform.jigsaw) {
                ucp = URLClassPath.class;
                JarLoader = Class.forName("jdk.internal.loader.URLClassPath$JarLoader");
                path_offset = -1;
                path_handle = ReflectionUtil.findVarHandle(ucp, "path", ArrayList.class);
                unopenedUrls_handle = ReflectionUtil.findVarHandle(ucp, "unopenedUrls", ArrayDeque.class);
                loaders_offset = -1;
                loaders_handle = ReflectionUtil.findVarHandle(ucp, "loaders", ArrayList.class);
                lmap_offset = -1;
                lmap_handle = ReflectionUtil.findVarHandle(ucp, "lmap", HashMap.class);
                jarHandler_offset = -1;
                jarHandler_handle = ReflectionUtil.findVarHandle(ucp, "jarHandler", URLStreamHandler.class);
            } else {
                ucp = sun.misc.URLClassPath.class;
                JarLoader = Class.forName("sun.misc.URLClassPath$JarLoader");
                path_offset = Unsafe.objectFieldOffset(ReflectionUtil.getField(ucp, "path"));
                path_handle = null;
                unopenedUrls_handle = null;
                loaders_offset = Unsafe.objectFieldOffset(ReflectionUtil.getField(ucp, "loaders"));
                loaders_handle = null;
                lmap_offset = Unsafe.objectFieldOffset(ReflectionUtil.getField(ucp, "lmap"));
                lmap_handle = null;
                jarHandler_offset = Unsafe.objectFieldOffset(ReflectionUtil.getField(ucp, "jarHandler"));
                jarHandler_handle = null;
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<URL> getPath(Object ucp) {
        if (Platform.jigsaw) {
            return (ArrayList<URL>) path_handle.get((URLClassPath) ucp);
        } else {
            return Unsafe.getObject(ucp, path_offset);
        }
    }

    public static void setPath(Object ucp, ArrayList<URL> neo) {
        if (Platform.jigsaw) {
            path_handle.set((URLClassPath) ucp, neo);
        } else {
            Unsafe.putObject(ucp, path_offset, neo);
        }
    }

    public static ArrayList<?> getLoaders(Object ucp) {
        if (Platform.jigsaw) {
            return (ArrayList<?>) loaders_handle.get((URLClassPath) ucp);
        } else {
            return Unsafe.getObject(ucp, loaders_offset);
        }
    }

    public static void setLoaders(Object ucp, ArrayList<?> neo) {
        if (Platform.jigsaw) {
            loaders_handle.set((URLClassPath) ucp, neo);
        } else {
            Unsafe.putObject(ucp, loaders_offset, neo);
        }
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, ?> getLmap(Object ucp) {
        if (Platform.jigsaw) {
            return (HashMap<String, ?>) lmap_handle.get((URLClassPath) ucp);
        } else {
            return Unsafe.getObject(ucp, lmap_offset);
        }
    }

    public static void setLmap(Object ucp, HashMap<String, ?> neo) {
        if (Platform.jigsaw) {
            lmap_handle.set((URLClassPath) ucp, neo);
        } else {
            Unsafe.putObject(ucp, lmap_offset, neo);
        }
    }

    public static URLStreamHandler getJarHandler(Object ucp) {
        if (Platform.jigsaw) {
            return (URLStreamHandler) jarHandler_handle.get((URLClassPath) ucp);
        } else {
            return Unsafe.getObject(ucp, jarHandler_offset);
        }
    }

    public static void setJarHandler(Object ucp, URLStreamHandler handler) {
        if (Platform.jigsaw) {
            jarHandler_handle.set((URLClassPath) ucp, handler);
        } else {
            Unsafe.putObject(ucp, jarHandler_offset, handler);
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayDeque<URL> getUnopenedUrls(URLClassPath ucp) {
        return (ArrayDeque<URL>) unopenedUrls_handle.get(ucp);
    }

    public static void setUnopenedUrls(URLClassPath ucp, ArrayDeque<URL> neo) {
        unopenedUrls_handle.set(ucp, neo);
    }

    public static boolean isJarLoader(Object o) {
        return JarLoader.isInstance(o);
    }
}
