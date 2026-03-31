package alice;

import alice._native.linux.mmap;
import alice._native.linux.mprotect;
import alice._native.linux.munmap;
import alice._native.win32.VirtualAlloc;
import alice._native.win32.VirtualFree;
import alice._native.win32.VirtualProtect;
import alice.exception.BadEnvironment;
import alice.injector.patch.PatcherLoader;
import alice.util.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.net.URLClassLoader;

import static org.objectweb.asm.Opcodes.*;

public class Init {

    private static void checkHSDB(){
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
            ClassUtil.append(FileUtil.getHSDB(), loader);
        }
    }

    private static void ensureASMLoaded() {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, "tmp", null, "java/lang/Object", null);
        cw.visitField(ACC_PUBLIC, "field", "I", null, null);
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "method", "()V", null, null);
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        cw.visitSource("114514", "191980");
        cw.toByteArray();
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
        PatcherLoader.load();
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
        if(!init){
            System.out.println("AliceAPI init.");
            init();
        }
    }
}
