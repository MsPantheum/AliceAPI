package alice.util;

import alice.Platform;
import jdk.internal.jimage.ImageReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class FileUtil {

    public static void createFile(String path) {
        createFile(Paths.get(path));
    }

    public static void createFile(Path path) {
        try {
            if (path.getParent() != null) {
                createDirectory(path.getParent());
            }
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

    public static final Path JAVA_HOME = Paths.get(System.getProperty("java.home").endsWith("jre") ? System.getProperty("java.home").substring(0, System.getProperty("java.home").length() - 3) : System.getProperty("java.home"));
    public static final Path TEMP_DIR = Paths.get(System.getProperty("java.io.tmpdir"));
    public static final Path SYSTEM_IMAGE = JAVA_HOME.resolve("lib").resolve("modules");

    public static Path getHSDB() {
        if (!Platform.jigsaw) {
            return search(JAVA_HOME, "sa-jdi.jar");
        } else {
            Path path = FileUtil.TEMP_DIR.resolve("jdk.hotspot.agent.jar");
            if (FileUtil.exists(path)) {
                FileUtil.delete(path);
            }
            Manifest manifest = new Manifest();
            try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(path), manifest)) {
                Path jmod = FileUtil.JAVA_HOME.resolve("jmods").resolve("jdk.hotspot.agent.jmod");
                if (exists(jmod)) {
                    try (ZipFile zip = new ZipFile(jmod.toFile())) {
                        Enumeration<? extends ZipEntry> entries = zip.entries();
                        while (entries.hasMoreElements()) {
                            ZipEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.startsWith("classes/") && name.endsWith(".class")) {
                                name = name.substring(8);
                                JarEntry je = new JarEntry(name);
                                jos.putNextEntry(je);
                                jos.write(IOUtil.getByteArray(zip.getInputStream(entry)));
                                jos.closeEntry();
                            }
                        }
                    }
                } else if (exists(SYSTEM_IMAGE)) {
                    try (ImageReader reader = ImageReader.open(SYSTEM_IMAGE)) {
                        new Runnable() {
                            @Override
                            public void run() {
                                ImageReader.Node node;
                                try {
                                    assert reader != null;
                                    node = reader.findNode("/modules/jdk.hotspot.agent");
                                    if (node.isDirectory()) {
                                        read(reader, node);
                                    } else {
                                        throw new IllegalStateException();
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            //I don't want to declare this method in FileUtil, so I just put it into an anonymous class.
                            private void read(ImageReader reader, ImageReader.Node node) throws IOException {
                                if (node.isDirectory()) {
                                    node.getChildNames().forEach(name -> {
                                        try {
                                            read(reader, reader.findNode(name));
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                                } else {
                                    String name = node.getName().substring(27);
                                    byte[] data = reader.getResource(node);
                                    JarEntry je = new JarEntry(name);
                                    jos.putNextEntry(je);
                                    jos.write(data);
                                    jos.closeEntry();
                                }
                            }
                        }.run();
                    }

                } else {
                    throw new IllegalStateException("Cannot read system classes!");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return path;
        }
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

    public static void delete(String path) {
        delete(Paths.get(path));
    }

    public static void delete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void append(Path path, String string) {
        try {
            Files.write(path, string.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
