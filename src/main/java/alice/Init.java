package alice;

import alice._native.linux.mmap;
import alice._native.linux.mprotect;
import alice._native.linux.munmap;
import alice._native.win32.VirtualAlloc;
import alice._native.win32.VirtualFree;
import alice._native.win32.VirtualProtect;
import alice.exception.BadEnvironment;
import alice.exception.ExitNow;
import alice.injector.ClassPatcher;
import alice.log.Logger;
import alice.util.*;
import com.google.common.base.Objects;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.util.Printer;

import java.nio.file.Files;

public class Init {

    /*
     * Check whether hotspot debugger database is in the classpath, if not, append it into classpath.
     */
    private static void checkHSDB() {
        ClassLoader loader = ClassLoader.getSystemClassLoader();

        try {
            Class<?> app = Class.forName(Platform.jigsaw ? "jdk.internal.loader.ClassLoaders$AppClassLoader" : "sun.misc.Launcher$AppClassLoader");
            if (loader.getClass() != app) {
                throw new BadEnvironment("Modified system classloader! Current is " + loader.getClass().getName() + " loaded by " + loader.getClass().getClassLoader());
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            loader.loadClass("sun.jvm.hotspot.utilities.UnsupportedPlatformException");
        } catch (ClassNotFoundException e) {
            Logger.MAIN.info("Append HSDB.");
            Logger.MAIN.info("Path: " + FileUtil.getHSDB());
            if (!Files.exists(FileUtil.getHSDB())) {
                throw new RuntimeException("THE FUCK?");
            }
            ClassUtil.append(FileUtil.getHSDB(), loader);
        }

    }

    private static boolean init = false;

    /**
     * Internal mechanism of AliceAPI initialization.
     */
    private static void init() {
        Thread.UncaughtExceptionHandler handle = (t, e) -> {
            if (!(e instanceof ExitNow)) {
                Logger.MAIN.fatal("Uncaught exception in thread " + t.getName());
                DebugUtil.printThrowableFully(e);
                Logger.MAIN.debug(String.valueOf(ProcessUtil.getPID()));
                ProcessUtil.guiPause();
            } else {
                Runtime.getRuntime().exit(-1);
            }
        };
        Runtime.getRuntime().addShutdownHook(new Thread(Logger::stopAll));
        if ("true".equals(System.getProperty("alice.debug"))) {
            Logger.enable(Logger.LogLevel.DEBUG);
            Logger.enable(Logger.LogLevel.TRACE);
        }
        Thread.setDefaultUncaughtExceptionHandler(handle);
        Thread.currentThread().setUncaughtExceptionHandler(handle);
        Unsafe.ensureClassInitialized(ReflectionUtil.class);
        if (Platform.jigsaw) {
            ModuleUtil.openAll();
        }
        Unsafe.ensureClassInitialized(Platform.class);
        String[] jars = new String[]{ClassUtil.getJarPath(Opcodes.class), ClassUtil.getJarPath(Analyzer.class), ClassUtil.getJarPath(Method.class), ClassUtil.getJarPath(ClassNode.class), ClassUtil.getJarPath(Printer.class), ClassUtil.getJarPath(Objects.class)};
        ClassUtil.ensureClassesInJarLoaded(jars);
        ClassPatcher.load();
        Logger.MAIN.info("ClassPatcher loaded.");
        checkHSDB();
        Logger.MAIN.info("HSDB is ok.");

        Unsafe.ensureClassInitialized(sun.jvm.hotspot.HSDB.class);

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

    /**
     * The init method of AliceAPI. This method should be called before any other thing.
     * If you are using LaunchWrapper you won't mind this.
     * Otherwise, you should call it in your main method.
     */
    public static synchronized void ensureInit() {
        if (!init) {
            Logger.MAIN.info("AliceAPI init.");
            init();
        }
    }
}
