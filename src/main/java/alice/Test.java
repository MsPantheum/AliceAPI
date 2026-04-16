package alice;

import alice.log.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Test {

    public static void test(URL url) {
        try {
            Logger.MAIN.debug("-------------------------------------------------");
            Logger.MAIN.debug("url: " + url);
            Logger.MAIN.debug("url path: " + url.getPath());
            Logger.MAIN.debug("url file: " + url.getFile());
            String s = url.toString();
            Logger.MAIN.debug("???: " + s.substring(s.indexOf("!/") + 2));

            URI uri = url.toURI();
            Logger.MAIN.debug("uri: " + uri);
            String path = uri.getPath();
            Logger.MAIN.debug("uri path: " + path);
            Logger.MAIN.debug("-------------------------------------------------");
        } catch (URISyntaxException e) {
            Logger.MAIN.debug("Failed to get uri!");
        }

    }
}
