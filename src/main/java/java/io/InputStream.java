package java.io;

public abstract class InputStream implements Closeable {
    public byte[] readAllBytes() throws IOException {
        return null;
    }

    public abstract int read() throws IOException;

    public int read(byte b[]) throws IOException {
        return 0;
    }
}
