package java.lang.module;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("all")
public interface ModuleReader extends Closeable {
    Optional<URI> find(String name) throws IOException;

    default Optional<InputStream> open(String name) throws IOException {
        return null;
    }

    default Optional<ByteBuffer> read(String name) throws IOException {
        return null;
    }

    default void release(ByteBuffer bb) {
    }

    Stream<String> list() throws IOException;

    @Override
    void close() throws IOException;
}
