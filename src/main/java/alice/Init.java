package alice;

import alice._native.ASMUtil;
import alice._native.KernelConfig;
import alice._native.jni.JNIInvokeInterface_.GetEnv;
import alice._native.jni.JNINativeInterface_.NewGlobalRef;
import alice._native.jni.JNI_GetCreatedJavaVMs;
import alice._native.linux.mmap;
import alice._native.linux.mprotect;
import alice._native.linux.munmap;
import alice._native.win32.VirtualAlloc;
import alice._native.win32.VirtualFree;
import alice._native.win32.VirtualProtect;
import alice.exception.BadEnvironment;
import alice.exception.ShouldNotReachHere;
import alice.injector.ClassPatcher;
import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;
import alice.log.Logger;
import alice.util.*;
import com.google.common.base.Objects;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.util.Printer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static alice._native.ASMUtil.Register.R10;

/**
 * Do the essential setup to use AliceAPI.
 */
public final class Init {

    /**
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
                throw new ShouldNotReachHere();
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
            Logger.MAIN.fatal("Uncaught exception in thread " + t.getName());
            DebugUtil.printThrowableFully(e);
            Logger.MAIN.fatal(String.valueOf(ProcessUtil.getPID()));
            e.printStackTrace(System.out);
            ProcessUtil.guiPause();
        };
        Thread.setDefaultUncaughtExceptionHandler(handle);
        Thread.currentThread().setUncaughtExceptionHandler(handle);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> LaunchWrapper.running = false));
        ReflectionUtil.load();
        if (Platform.jigsaw) {
            ModuleUtil.openAll();
        }
        Unsafe.ensureClassInitialized(Platform.class);
        String[] jars = new String[]{FileUtil.getJarPath(Opcodes.class), FileUtil.getJarPath(Analyzer.class), FileUtil.getJarPath(Method.class), FileUtil.getJarPath(ClassNode.class), FileUtil.getJarPath(Printer.class), FileUtil.getJarPath(Objects.class)};
        ClassUtil.ensureClassesInJarLoaded(jars);
        ClassPatcher.load();
        Logger.MAIN.info("ClassPatcher loaded.");
        checkHSDB();
        Logger.MAIN.info("HSDB is ok.");

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
        Unsafe.ensureClassInitialized(JNI_GetCreatedJavaVMs.class);
        Unsafe.ensureClassInitialized(GetEnv.class);
        Unsafe.ensureClassInitialized(NewGlobalRef.class);

        if (Platform.win32) {
            try {
                System.loadLibrary("advapi32");
            } catch (UnsatisfiedLinkError ignored) {
            }

        }

        if ("true".equals(System.getProperty("alice.enchanced_environment_detection"))) {
            if (Platform.win32) {
                long function = SymbolLookup.lookupValue("NtQuerySystemInformation");
                byte[] raw = new byte[]{(byte) 0x55, (byte) 0x48, (byte) 0x89, (byte) 0xe5, (byte) 0x48, (byte) 0x83, (byte) 0xec, (byte) 0x30, (byte) 0xc7, (byte) 0x45, (byte) 0xf0, (byte) 0x8, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0xc7, (byte) 0x45, (byte) 0xf4, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0xb9, (byte) 0x67, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x48, (byte) 0x8d, (byte) 0x55, (byte) 0xf0, (byte) 0x41, (byte) 0xb8, (byte) 0x8, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x4c, (byte) 0x8d, (byte) 0x4d, (byte) 0xf8, (byte) 0x41, (byte) 0xff, (byte) 0xd2, (byte) 0x85, (byte) 0xc0, (byte) 0x75, (byte) 0x10, (byte) 0x8b, (byte) 0x45, (byte) 0xf4, (byte) 0x83, (byte) 0xe0, (byte) 0x2, (byte) 0x74, (byte) 0x4, (byte) 0xb0, (byte) 0x1, (byte) 0xeb, (byte) 0x6, (byte) 0x30, (byte) 0xc0, (byte) 0xeb, (byte) 0x2, (byte) 0x30, (byte) 0xc0, (byte) 0x48, (byte) 0x89, (byte) 0xec, (byte) 0x5d, (byte) 0xc3};
                byte[] payload = new byte[raw.length + 10];
                System.arraycopy(raw, 0, payload, 0, 8);
                ASMUtil.movabs(payload, 8, R10, function);
                System.arraycopy(raw, 8, payload, 18, raw.length - 8);
                int value = MethodInjector.runShellcodeNativeInt(payload);
                if (value == 1) {
                    throw new BadEnvironment("Suspicious environment detected!");
                }
            } else {
                if (KernelConfig.isConfigBuiltin("CONFIG_MODULES")) {
                    Path p = Paths.get("/sys/module/module/parameters/sig_enforce");
                    if (Files.exists(p)) {
                        String s = new String(FileUtil.read(p));
                        if (s.equals("N") || s.equals("0")) {
                            throw new BadEnvironment("Suspicious environment detected!");
                        }
                    } else {
                        throw new BadEnvironment("Suspicious environment detected!");
                    }
                }
            }
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
