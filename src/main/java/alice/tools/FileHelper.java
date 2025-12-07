package alice.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileHelper {
    public static Path search(Path dir, String name) {
        if (!Files.isDirectory(dir)) {
            throw new IllegalArgumentException();
        }
        final Path[] ret = new Path[1];
        try (Stream<Path> stream = Files.list(dir)) {
            stream.forEach(p -> {
                if (Files.isDirectory(p)) {
                    ret[0] = ret[0] == null ? search(p, name) : ret[0];
                } else {
                    System.out.println("name " + p.getFileName());
                    if (p.getFileName().toString().equals(name)) {
                        ret[0] = p;
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ret[0];
    }
}
