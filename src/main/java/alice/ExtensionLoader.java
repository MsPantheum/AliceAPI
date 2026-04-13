package alice;

import alice.injector.ClassPatcher;
import alice.log.Logger;
import alice.util.ClassUtil;
import alice.util.FileUtil;
import alice.util.ReflectionUtil;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.nio.file.Path;
import java.util.jar.JarFile;

/**
 * Simple extension loader.<br>
 * Extension should specify an extension class through manifest attribute Alice-Extension.<br>
 * Extension class should have a static method load()V.<br>
 * By default, loader will look up extensions from directory alice in working directory.<br>
 * Or you can specify extension directory by setting property alice.extension.directory.
 */
public class ExtensionLoader {
    public static void load(){
        String extension_dir = System.getProperty("alice.extension.directory");
        if(extension_dir == null) {
            extension_dir = "alice";
        }
        if(!FileUtil.isDirectory(extension_dir)){
            FileUtil.createDirectory(extension_dir);
        }
        Logger.MAIN.info("Scanning extensions.");
        for (Path path : FileUtil.list(extension_dir)) {
            if(path.toString().endsWith(".jar")){
                ClassUtil.append(path, ClassLoader.getSystemClassLoader());
                ClassPatcher.addProtectedJar(path.toString());
                try (JarFile jar = new JarFile(path.toFile())){
                    String target = jar.getManifest().getMainAttributes().getValue("Alice-Extension");
                    if(target == null){
                        continue;
                    }
                    try {
                        Class<?> loader_class = Class.forName(target);
                        MethodHandle load = ReflectionUtil.findStatic(loader_class,"load", MethodType.methodType(void.class));
                        load.invoke();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
