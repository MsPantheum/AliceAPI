package alice;

import alice.util.ClassUtil;
import alice.util.FileUtil;
import alice.util.ReflectionUtil;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.jar.JarFile;

public class ExtensionLoader {
    public static void load(){
        String extension_dir = System.getProperty("alice.extension.directory");
        if(extension_dir == null) {
            extension_dir = "alice";
        }
        if(!FileUtil.isDirectory(extension_dir)){
            FileUtil.createDirectory(extension_dir);
        }
        System.out.println("Scanning extensions.");
        for (Path path : FileUtil.list(extension_dir)) {
            if(path.toString().endsWith(".jar")){
                ClassUtil.append(path, (URLClassLoader) ClassLoader.getSystemClassLoader());
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
