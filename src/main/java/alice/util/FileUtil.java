package alice.util;

import java.io.IOException;
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
        try(Stream<Path> stream = Files.list(Paths.get(dir))){
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

    public static boolean exists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
}
