package jdk.internal.access;

import java.net.URL;
import java.net.URLStreamHandler;

public interface JavaNetURLAccess {
    URLStreamHandler getHandler(URL u);
}
