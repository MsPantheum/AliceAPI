package alice.util;

import alice.Platform;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class IOUtil {

    public static InputStream getInputStream(byte[] data) {
        return new ByteArrayInputStream(data);
    }

    public static byte[] getByteArray(InputStream in) throws IOException {
        if (Platform.jigsaw) {
            return in.readAllBytes();
        }
        return IOUtils.toByteArray(in);
    }

    public static byte[] readURL(URL url) throws IOException {
        return getByteArray(url.openStream());
    }
}
