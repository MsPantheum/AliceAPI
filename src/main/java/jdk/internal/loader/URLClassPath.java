package jdk.internal.loader;

import java.io.IOException;
import java.net.URL;
import java.security.AccessControlContext;
import java.util.Enumeration;

public class URLClassPath {

    public URLClassPath(URL[] urLs, AccessControlContext context) {

    }

    public URL[] getURLs() {
        return null;
    }

    public synchronized void addURL(URL url) {
    }

    public URL findResource(String name, boolean check) throws IOException {
        return null;
    }

    public Resource getResource(String name, boolean check) {
        return null;
    }

    public Enumeration<URL> findResources(final String name,
                                          final boolean check) {
        return null;
    }

    public Resource getResource(String name) {
        return null;
    }

    public Enumeration<Resource> getResources(final String name,
                                              final boolean check) {
        return null;
    }

    public Enumeration<Resource> getResources(final String name) {
        return null;
    }

}
