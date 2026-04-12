package alice;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Test {
    public static void test(URL url) {
        try {
            System.out.println("-------------------------------------------------");
            System.out.println("url: " + url);
            System.out.println("url path: " + url.getPath());
            URI uri = url.toURI();
            System.out.println("uri: " + uri);
            String path = uri.getPath();
            System.out.println("path: " + path);
            System.out.println("-------------------------------------------------");
        } catch (URISyntaxException e) {
            System.out.println("Failed to get uri!");
        }

    }
}
