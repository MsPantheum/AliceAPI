package alice;

import alice._native.linux.mmap;
import alice._native.linux.mprotect;
import alice._native.linux.munmap;
import alice._native.win32.VirtualAlloc;
import alice._native.win32.VirtualFree;
import alice._native.win32.VirtualProtect;
import alice.exception.BadEnvironment;
import alice.injector.patch.ClassPatcher;
import alice.util.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.util.Printer;

import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Init {

    private static void checkHSDB() {

        URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();

        try {
            Class<?> app = loader.loadClass("sun.misc.Launcher$AppClassLoader");
            if (loader.getClass() != app) {
                throw new BadEnvironment("Modified system classloader! Current is " + loader.getClass().getName() + " loaded by " + loader.getClass().getClassLoader());
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            loader.loadClass("sun.jvm.hotspot.utilities.UnsupportedPlatformException");
        } catch (ClassNotFoundException e) {
            System.out.println("Append HSDB.");
            System.out.println("Path: " + FileUtil.getHSDB());
            if (!Files.exists(FileUtil.getHSDB())) {
                throw new RuntimeException("THE FUCK?");
            }
            ClassUtil.append(FileUtil.getHSDB(), loader);
        }

    }

    private static void ensureASMLoaded() {
        String[] jars = new String[]{ClassUtil.getJarPath(Opcodes.class), ClassUtil.getJarPath(Analyzer.class), ClassUtil.getJarPath(Method.class), ClassUtil.getJarPath(ClassNode.class), ClassUtil.getJarPath(Printer.class)};
        for (String path : jars) {
            try (JarFile jar = new JarFile(path)) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if(name.endsWith(".class") && !name.endsWith("module-info.class")){
                        name = name.substring(0,name.length() - 6).replace('/','.');
                        try {
                            Unsafe.ensureClassInitialized(Class.forName(name));
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static boolean init = false;

    private static void init() {
        Thread.UncaughtExceptionHandler handle = (t, e) -> {
            System.err.println("Uncaught exception in thread " + t.getName());
            DebugUtil.printThrowableFully(e);
            System.out.println(ProcessUtil.getPID());
            ProcessUtil.guiPause();
        };
        Thread.setDefaultUncaughtExceptionHandler(handle);
        Thread.currentThread().setUncaughtExceptionHandler(handle);
        checkHSDB();
        System.out.println("HSDB is ok.");
        Unsafe.ensureClassInitialized(Platform.class);
        ensureASMLoaded();
        ClassPatcher.load();
        System.out.println("UCP patch loaded.");
        Unsafe.ensureClassInitialized(HSDB.class);
        if (!Platform.win32) {
            Unsafe.ensureClassInitialized(mprotect.class);
            Unsafe.ensureClassInitialized(mmap.class);
            Unsafe.ensureClassInitialized(munmap.class);
        } else {
            Unsafe.ensureClassInitialized(VirtualProtect.class);
            Unsafe.ensureClassInitialized(VirtualAlloc.class);
            Unsafe.ensureClassInitialized(VirtualFree.class);
        }
        init = true;
    }

    public static synchronized void ensureInit() {
        if (!init) {
            System.out.println("AliceAPI init.");
            init();
        }
    }
}
