package alice.util;

import alice.Platform;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

public class FileUtil {
    public static void write(String path,byte[] data){
        try {
            Files.write(Paths.get(path),data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createDirectory(String path){
        Path dir = Paths.get(path);
        if(Files.exists(dir) && !Files.isDirectory(dir)){
            throw new IllegalStateException("File already exists!");
        }
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path[] list(String path){
        Path dir = Paths.get(path);
        try (Stream<Path> files = Files.list(dir)){
            return files.toArray(Path[]::new);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] read(String path){
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString(String path){
        try {
            return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path search(String dir,String name){

        Path ret = null;
        Path dir_p = Paths.get(dir);
        if(!Files.isDirectory(dir_p)){
            return null;
        }
        try(Stream<Path> stream = Files.list(dir_p)){
            Iterator<Path> iterator = stream.iterator();
            while (iterator.hasNext()){
                Path p = iterator.next();
                if(Files.isDirectory(p)){
                    ret = search(p.toString(),name);
                    if(ret != null){
                        break;
                    }
                } else {
                    if(name.equals(p.getFileName().toString())){
                        ret = p;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    public static String getJavaHome() {
        String HOME = System.getProperty("java.home");
        return HOME.endsWith("jre") ? HOME.substring(0, HOME.length()-3) : HOME;
    }

    public static Path getHSDB(){
        return search(getJavaHome(),"sa-jdi.jar");
    }

    public static boolean exists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    public static boolean isDirectory(String path) {
        return Files.isDirectory(Paths.get(path));
    }

    public static String getJarPath(Class<?> cls) {
        String tmp = cls.getProtectionDomain().getCodeSource().getLocation().getPath().replace("!/" + cls.getName().replace('.', '/'), "").replace("file:", "");
        if (Platform.win32) {
            tmp = tmp.substring(1);
        }

        try {
            tmp = URLDecoder.decode(tmp, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ignored) {
        }

        return tmp;
    }
}
