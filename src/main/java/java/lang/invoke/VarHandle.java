package java.lang.invoke;

import jdk.internal.access.JavaNetURLAccess;
import jdk.internal.access.JavaNetUriAccess;
import jdk.internal.access.JavaUtilJarAccess;
import jdk.internal.loader.BuiltinClassLoader;
import jdk.internal.loader.URLClassPath;
import sun.jvm.hotspot.HSDB;
import sun.jvm.hotspot.HotSpotAgent;

import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.*;
import java.util.function.BiFunction;

public class VarHandle {
    public native void set(Module a1, Map<?, ?> a2);

    public native void set(ModuleDescriptor a1, boolean a2);

    public native void set(ModuleDescriptor a1, Set<?> a2);

    public native Object get(BuiltinClassLoader loader);

    public native Object get();

    public native void set(JavaUtilJarAccess neo);

    public native void set(JavaNetURLAccess neo);

    public native void set(JavaNetUriAccess neo);

    public native void set(URLClassPath ucp, URLStreamHandler handler);

    public native void set(BiFunction<?, ?, ?> resourceConsumer);

    public native void set(HSDB hsdb, HotSpotAgent agent);

    public native Object get(HotSpotAgent agent);

    public void set(BuiltinClassLoader loader, URLClassPath ucp) {
    }

    public void set(ClassLoader loader, Map<ModuleReference, ModuleReader> m2r) {
    }

    public Object get(URLClassPath ucp) {
        return null;
    }

    public void set(URLClassPath ucp, ArrayDeque<?> neo) {
    }

    public void set(URLClassPath ucp, ArrayList<?> neo) {

    }

    public void set(URLClassPath ucp, HashMap<?, ?> neo) {

    }

    public Object get(URLClassLoader loader) {
        return null;
    }
}
