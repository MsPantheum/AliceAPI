package alice.tools;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SymbolFinder {

    private static final Object libjvm;
    private static final MethodHandle lookup;

    private static String findJVM() {
        Path home = Paths.get(System.getProperty("java.home"));
        home = FileHelper.search(home, System.mapLibraryName("jvm"));
        if (home == null) {
            return null;
        }
        return home.toString();
    }

    public static void main(String[] args) {
        System.out.println(findJVM());
    }

    static {
        try {
            Class<?> nl = Class.forName("java.lang.ClassLoader$NativeLibrary");
            libjvm = UnsafeHelper.allocateInstance(nl);
            Field f = nl.getDeclaredField("name");
            UnsafeHelper.putObject(libjvm, f, findJVM());
            MethodHandle load = ReflectionHelper.findVirtual(nl, "load", MethodType.methodType(void.class));
            load.invoke(nl, findJVM(), false);
            lookup = ReflectionHelper.findVirtual(nl, "find", MethodType.methodType(long.class));
            lookup.bindTo(nl);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long findSymbol(String name) {
        try {
            return (long) lookup.invoke(name);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
