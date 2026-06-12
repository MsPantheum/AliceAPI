package jdk.internal.loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.CodeSigner;
import java.util.jar.Manifest;

public abstract class Resource {
    public abstract String getName();

    public abstract URL getURL();

    public abstract URL getCodeSourceURL();

    public abstract InputStream getInputStream() throws IOException;

    public abstract int getContentLength() throws IOException;

    public byte[] getBytes() throws IOException {
        return null;
    }

    public ByteBuffer getByteBuffer() {
        return null;
    }

    public Manifest getManifest() {
        return null;
    }

    public java.security.cert.Certificate[] getCertificates() {
        return null;
    }

    public CodeSigner[] getCodeSigners() {
        return null;
    }

    public Exception getDataError() {
        return null;
    }
}
