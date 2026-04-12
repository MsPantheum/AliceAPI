package alice;

import alice.api.ClassByteProcessor;
import alice.exception.BadEnvironment;
import alice.injector.ClassPatcher;
import alice.injector.Overrider;
import alice.injector.classloading.*;
import alice.util.*;
import org.objectweb.asm.Opcodes;
import sun.jvm.hotspot.HotSpotAgent;
import sun.misc.URLClassPath;

import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.function.BiFunction;

public class TTTest implements Opcodes {

    static {
        ModuleUtil.openAll();
        if (Platform.jigsaw) {
            Unsafe.ensureClassInitialized(jdk.internal.loader.URLClassPath.class);
            Unsafe.ensureClassInitialized(Module2Reader.class);
            Unsafe.ensureClassInitialized(WrappedModuleReader.class);
            Unsafe.ensureClassInitialized(ResourceWrapper.StaticResource.class);
            Unsafe.ensureClassInitialized(CollectionWrapper.StaticResources.class);
        } else {
            Unsafe.ensureClassInitialized(URLClassLoader.class);
            Unsafe.ensureClassInitialized(URLClassPath.class);
            Unsafe.ensureClassInitialized(UCPWrapper.LegacyURLClassPathWrapper.class);
            Unsafe.ensureClassInitialized(ResourceWrapper.LegacyResource.LegacyStaticResource.class);
            Unsafe.ensureClassInitialized(CollectionWrapper.LegacyStaticResources.class);
        }

        Unsafe.ensureClassInitialized(ClassPatcher.class);
        Unsafe.ensureClassInitialized(UCPUtil.class);
        Unsafe.ensureClassInitialized(UCPWrapper.StaticURLs.class);
        Unsafe.ensureClassInitialized(ClassByteProcessor.class);
    }

    public static void main(String[] args) throws Throwable {
        Class<?> target = Class.forName("jdk.internal.loader.URLClassPath$JarLoader");
        Class<?> override = Overrider.override(target, (method, desc) -> {
            if (method.equals("getResource") && desc.equals("(Ljava/lang/String;Z)Ljdk/internal/loader/Resource;")) {
                return mv -> {
                    mv.visitFieldInsn(GETSTATIC, target.getName().replace('.', '/') + "Overrides", "resourceProcessor", "Ljava/util/function/BiFunction;");
                    mv.visitInsn(SWAP);
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitMethodInsn(INVOKEINTERFACE, "java/util/function/BiFunction", "apply", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
                    mv.visitTypeInsn(CHECKCAST, "jdk/internal/loader/Resource");
                };
            }
            return null;
        }, cw -> cw.visitField(ACC_PRIVATE | ACC_STATIC, "resourceProcessor", "Ljava/util/function/BiFunction;", "Ljava/util/function/BiFunction<Lsun/misc/Resource;Ljava/lang/String;>;", null));
        ReflectionUtil.findStaticVarHandle(override, "resourceProcessor", BiFunction.class).set(ResourceWrapper.resourceConsumer);
        System.out.println("Success.");
        System.out.println(override.getName());
        Object instance = Unsafe.allocateInstance(override);
        ReflectionUtil.findVarHandle(override, "delegate", target).set(instance, getTestInstance());
        ArrayList<Object> neo = new ArrayList<>();
        neo.add(instance);
        UCPUtil.setLoaders(ClassLoaderUtil.getUCP(ClassLoader.getSystemClassLoader()), neo);
        System.out.println("Start to test.");
        checkHSDB();
        Unsafe.ensureClassInitialized(HotSpotAgent.class);
        Unsafe.ensureClassInitialized(Meow.class);
        Unsafe.ensureClassInitialized(ExtensionLoader.class);
        Unsafe.ensureClassInitialized(HDE64.class);
        Unsafe.ensureClassInitialized(ClassByteProcessor.class);
    }

    private static Object getTestInstance() {
        return UCPUtil.getLoaders(ClassLoaderUtil.getUCP(ClassLoader.getSystemClassLoader())).get(0);
    }

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
            System.out.println("Append HSDB.");
            System.out.println("Path: " + FileUtil.getHSDB());
            if (!Files.exists(FileUtil.getHSDB())) {
                throw new RuntimeException("THE FUCK?");
            }
            ClassUtil.append(FileUtil.getHSDB(), loader);
        }

    }
}
