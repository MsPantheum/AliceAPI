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
import com.google.common.base.Objects;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.util.Printer;

import java.net.URLClassLoader;
import java.nio.file.Files;

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
        String[] jars = new String[]{ClassUtil.getJarPath(Opcodes.class), ClassUtil.getJarPath(Analyzer.class), ClassUtil.getJarPath(Method.class), ClassUtil.getJarPath(ClassNode.class), ClassUtil.getJarPath(Printer.class), ClassUtil.getJarPath(Objects.class)};
        ClassUtil.ensureClassesInJarLoaded(jars);
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
