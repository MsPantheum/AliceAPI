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

    public static void createFile(String path) {
        createFile(Paths.get(path));
    }

    public static void createFile(Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(String path, byte[] data) {
        write(Paths.get(path), data);
    }

    public static void write(Path path, byte[] data) {
        try {
            if (path.getParent() != null) {
                createDirectory(path.getParent());
            }
            if (!exists(path)) {
                createFile(path);
            }
            Files.write(path, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createDirectory(String path) {
        createDirectory(Paths.get(path));
    }

    public static void createDirectory(Path path) {
        if (isDirectory(path)) {
            return;
        }
        if (exists(path)) {
            throw new IllegalStateException("File already exists!");
        }

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path[] list(String path) {
        return list(Paths.get(path));
    }

    public static Path[] list(Path path) {

        try (Stream<Path> files = Files.list(path)) {
            return files.toArray(Path[]::new);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] read(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] read(String path) {
        return read(Paths.get(path));
    }

    public static String toString(Path path) {
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString(String path) {
        return toString(Paths.get(path));
    }

    public static Path search(Path dir, String name) {
        Path ret = null;
        if (!isDirectory(dir)) {
            throw new IllegalArgumentException("dir is not a directory!");
        }
        try (Stream<Path> stream = Files.list(dir)) {
            Iterator<Path> iterator = stream.iterator();
            while (iterator.hasNext()) {
                Path p = iterator.next();
                if (isDirectory(p)) {
                    ret = search(p.toString(), name);
                    if (ret != null) {
                        break;
                    }
                } else {
                    if (name.equals(p.getFileName().toString())) {
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

    public static Path search(String dir, String name) {
        return search(Paths.get(dir), name);
    }

    public static String getJavaHome() {
        String HOME = System.getProperty("java.home");
        return HOME.endsWith("jre") ? HOME.substring(0, HOME.length() - 3) : HOME;
    }

    public static Path getHSDB() {
        return search(getJavaHome(), "sa-jdi.jar");
    }

    public static boolean exists(String path) {
        return exists(Paths.get(path));
    }

    public static boolean exists(Path path) {
        return Files.exists(path);
    }

    public static boolean isDirectory(Path path) {
        return Files.isDirectory(path);
    }

    public static boolean isDirectory(String path) {
        return isDirectory(Paths.get(path));
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
