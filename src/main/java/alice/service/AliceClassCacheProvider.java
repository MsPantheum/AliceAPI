package alice.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AliceClassCacheProvider extends FileSystemProvider {

    private static class AlicePath implements Path {

        private final AliceFileSystem fileSystem;

        private AlicePath(AliceFileSystem fileSystem) {
            this.fileSystem = fileSystem;
        }

        @Override
        public FileSystem getFileSystem() {
            return fileSystem;
        }

        @Override
        public boolean isAbsolute() {
            return false;
        }

        @Override
        public Path getRoot() {
            return null;
        }

        @Override
        public Path getFileName() {
            return null;
        }

        @Override
        public Path getParent() {
            return null;
        }

        @Override
        public int getNameCount() {
            return 0;
        }

        @Override
        public Path getName(int index) {
            return null;
        }

        @Override
        public Path subpath(int beginIndex, int endIndex) {
            return null;
        }

        @Override
        public boolean startsWith(Path other) {
            return false;
        }

        @Override
        public boolean startsWith(String other) {
            return false;
        }

        @Override
        public boolean endsWith(Path other) {
            return false;
        }

        @Override
        public boolean endsWith(String other) {
            return false;
        }

        @Override
        public Path normalize() {
            return null;
        }

        @Override
        public Path resolve(Path other) {
            return null;
        }

        @Override
        public Path resolve(String other) {
            return null;
        }

        @Override
        public Path resolveSibling(Path other) {
            return null;
        }

        @Override
        public Path resolveSibling(String other) {
            return null;
        }

        @Override
        public Path relativize(Path other) {
            return null;
        }

        @Override
        public URI toUri() {
            return null;
        }

        @Override
        public Path toAbsolutePath() {
            return null;
        }

        @Override
        public Path toRealPath(LinkOption... options) throws IOException {
            return null;
        }

        @Override
        public File toFile() {
            return null;
        }

        @Override
        public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
            return null;
        }

        @Override
        public WatchKey register(WatchService watcher, WatchEvent.Kind<?>... events) throws IOException {
            return null;
        }

        @Override
        public Iterator<Path> iterator() {
            return null;
        }

        @Override
        public int compareTo(Path other) {
            return 0;
        }
    }

    private static class AliceFileSystem extends FileSystem {

        private final AliceClassCacheProvider provider;

        private AliceFileSystem(AliceClassCacheProvider provider) {
            this.provider = provider;
        }

        @Override
        public FileSystemProvider provider() {
            return provider;
        }

        @Override
        public void close() {
        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public boolean isReadOnly() {
            return true;
        }

        @Override
        public String getSeparator() {
            return "/";
        }

        @Override
        public Iterable<Path> getRootDirectories() {
            return null;
        }

        @Override
        public Iterable<FileStore> getFileStores() {
            return null;
        }

        @Override
        public Set<String> supportedFileAttributeViews() {
            return Collections.emptySet();
        }

        @Override
        public Path getPath(String first, String... more) {
            return null;
        }

        @Override
        public PathMatcher getPathMatcher(String syntaxAndPattern) {
            return null;
        }

        @Override
        public UserPrincipalLookupService getUserPrincipalLookupService() {
            return null;
        }

        @Override
        public WatchService newWatchService() throws IOException {
            return null;
        }
    }

    private final AliceFileSystem fileSystem = new AliceFileSystem(this);

    @Override
    public String getScheme() {
        return "AliceClassCache";
    }

    @Override
    public FileSystem newFileSystem(URI uri, Map<String, ?> env) {
        return fileSystem;
    }

    @Override
    public FileSystem getFileSystem(URI uri) {
        return fileSystem;
    }

    @Override
    public Path getPath(URI uri) {
        return null;
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Path path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void copy(Path source, Path target, CopyOption... options) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void move(Path source, Path target, CopyOption... options) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSameFile(Path path, Path path2) {
        return path.equals(path2);
    }

    @Override
    public boolean isHidden(Path path) {
        return false;
    }

    @Override
    public FileStore getFileStore(Path path) {
        return null;
    }

    @Override
    public void checkAccess(Path path, AccessMode... modes) {
    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttribute(Path path, String attribute, Object value, LinkOption... options) {
        throw new UnsupportedOperationException();
    }

}
