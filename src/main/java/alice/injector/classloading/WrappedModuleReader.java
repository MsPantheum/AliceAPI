package alice.injector.classloading;

import alice.injector.ClassPatcher;
import alice.log.Logger;
import alice.util.ClassUtil;
import alice.util.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.module.ModuleReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

public class WrappedModuleReader implements ModuleReader {

    private final ModuleReader delegate;

    public WrappedModuleReader(ModuleReader delegate) {
        this.delegate = delegate;
        Logger.MAIN.debug("Wrap module reader: ".concat(delegate.toString()));
    }

    private static URI processURI(URI uri, String name) {
        if (name.startsWith("META-INF/services/") && !name.endsWith(".class")) {
            return uri;
        }
        System.out.println(name);
        byte[] data;
        try {
            if (uri.getScheme().equals("jar")) {
                String[] parts = uri.toString().split("!");
                URI fileUri = URI.create(parts[0]);
                String internalPath = parts[1];
                try (FileSystem fs = FileSystems.newFileSystem(fileUri, Collections.emptyMap())) {
                    data = FileUtil.read(fs.getPath(internalPath));
                }
            } else if (uri.getScheme().equals("file")) {
                data = FileUtil.read(Paths.get(uri));
            } else {
                data = uri.toURL().openConnection().getInputStream().readAllBytes();
            }
            assert data != null;
            if (data.length < 4 || !ClassUtil.isClassFile(data, 0)) {
                if (data.length < 4 && name.endsWith(".class")) {
                    Logger.MAIN.warn("Suspicious resource: ".concat(name));
                }
                return uri;
            }
            data = ClassPatcher.runTransformers(data, name);
            URL url = ClassPatcher.create(data, uri.toURL());
            return url.toURI();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<URI> find(String name) throws IOException {
        Optional<URI> _try = delegate.find(name);
        return _try.map(uri -> processURI(uri, name));
    }

    @Override
    public Optional<InputStream> open(String name) throws IOException {
        Optional<InputStream> _try = delegate.open(name);
        if (_try.isPresent()) {
            byte[] data = _try.get().readAllBytes();
            if (ClassUtil.isClassFile(data, 0)) {
                data = ClassPatcher.runTransformers(data, name);
                return Optional.of(new ByteArrayInputStream(data));
            }
        }
        return _try;
    }

    @Override
    public Optional<ByteBuffer> read(String name) throws IOException {
        Optional<ByteBuffer> _try = delegate.read(name);
        if (_try.isPresent()) {
            ByteBuffer buffer = _try.get();
            byte[] array = buffer.hasArray() ? buffer.array() : new byte[buffer.remaining()];
            if (!buffer.hasArray()) {
                buffer.get(array);
            }
            byte[] data = ClassPatcher.runTransformers(array, name);
            return Optional.of(ByteBuffer.wrap(data));
        }
        return _try;
    }

    @Override
    public void release(ByteBuffer bb) {
        delegate.release(bb);
    }

    @Override
    public Stream<String> list() throws IOException {
        return delegate.list();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}
