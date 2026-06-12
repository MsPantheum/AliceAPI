package jdk.internal.jimage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

public class ImageReader implements AutoCloseable {
    public static ImageReader open(Path imagePath) throws IOException {
        return null;
    }

    public Node findNode(String name) throws IOException {
        return null;
    }

    public Node findResourceNode(String moduleName, String resourcePath) {
        return null;
    }

    public boolean containsResource(String moduleName, String resourcePath)
            throws IOException {
        return false;
    }

    public byte[] getResource(Node node) throws IOException {
        return null;
    }

    public ByteBuffer getResourceBuffer(Node node) {
        return null;
    }

    @Override
    public void close() throws IOException {
    }

    public abstract static class Node {
        public final String getName() {
            return "null";
        }

        public final BasicFileAttributes getFileAttributes() {
            return null;
        }

        public final Node resolveLink() {
            return null;
        }

        public Node resolveLink(boolean recursive) {
            return null;
        }

        public boolean isLink() {
            return false;
        }

        public boolean isDirectory() {
            return false;
        }

        public boolean isResource() {
            return false;
        }

        public Stream<String> getChildNames() {
            return null;
        }

        public long size() {
            return 0L;
        }

        public long compressedSize() {
            return 0L;
        }

        public String extension() {
            return null;
        }
    }
}
