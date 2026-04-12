package java.lang.invoke;

import jdk.internal.access.JavaNetURLAccess;
import jdk.internal.access.JavaNetUriAccess;
import jdk.internal.access.JavaUtilJarAccess;
import jdk.internal.loader.URLClassPath;

import java.lang.module.ModuleDescriptor;
import java.net.URLStreamHandler;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class VarHandle {
    public native void set(Module a1, Map<?, ?> a2);

    public native void set(ModuleDescriptor a1, boolean a2);

    public native void set(ModuleDescriptor a1, Set<?> a2);

    public native Object get(Object... args);

    public native Object get();

    public void set(JavaUtilJarAccess neo) {
    }

    public void set(JavaNetURLAccess neo) {

    }

    public void set(JavaNetUriAccess neo) {

    }

    public void set(URLClassPath ucp, URLStreamHandler handler) {

    }

    public void set(Object instance, Object testInstance) {

    }

    public void set(BiFunction<?, ?, ?> resourceConsumer) {

    }
}
