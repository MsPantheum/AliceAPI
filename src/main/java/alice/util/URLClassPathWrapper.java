package alice.util;

import alice.injector.patch.ClassPatcher;
import org.apache.commons.io.IOUtils;
import sun.misc.Resource;
import sun.misc.URLClassPath;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Enumeration;
import java.util.List;

public class URLClassPathWrapper extends URLClassPath {

    private final URLClassPath delegate;

    public URLClassPathWrapper(URLClassPath delegate) {
        super(delegate.getURLs());
        this.delegate = delegate;
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof URLClassPathWrapper) {
            return ((URLClassPathWrapper) obj).delegate.equals(delegate);
        } else if (obj instanceof URLClassPath) {
            return obj.equals(delegate);
        }
        return super.equals(obj);
    }

    private static Resource processResource(Resource resource, String name) throws IOException {
        if (resource == null) {
            return null;
        }
        if (ClassPatcher.shouldRunTransformers()) {
            byte[] data = ClassPatcher.runTransformers(resource.getBytes(), name);
            InputStream is = new ByteArrayInputStream(data);
            String _name = resource.getName();
            URL _url = resource.getURL();
            URL _cs_url = resource.getCodeSourceURL();
            int length = data.length;
            return new StaticResource(_name, _url, _cs_url, is, length);
        }
        return resource;
    }

    @Override
    public Resource getResource(String name, boolean check) {
        try {
            return processResource(delegate.getResource(name, check), name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void addURL(URL url) {
        delegate.addURL(url);
    }

    @Override
    public synchronized List<IOException> closeLoaders() {
        return delegate.closeLoaders();
    }

    @Override
    public URL[] getURLs() {
        URL[] ret = delegate.getURLs();
        for (int i = 0; i < ret.length; i++) {
            URL url = ret[i];
            String path = url.getPath();
            if (path.endsWith(".class")) {
                try {
                    ret[i] = processURL(url, path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return ret;
    }

    @Override
    public Enumeration<Resource> getResources(String name, boolean check) {
        return new StaticResources(delegate.getResources(name, check));
    }

    @Override
    public URL checkURL(URL url) {
        return url;
    }

    @Override
    public URL findResource(String name, boolean check) {
        URL url = delegate.findResource(name, check);
        try {
            return name.endsWith(".class") ? processURL(url, name) : url;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static URL processURL(URL url, String name) throws IOException {
        if (url == null) {
            return null;
        }
        if (ClassPatcher.shouldRunTransformers()) {
            byte[] data = ClassPatcher.runTransformers(IOUtils.toByteArray(url), name);
            return new URL("mem", null, -1, "", new URLStreamHandler() {
                @Override
                protected URLConnection openConnection(URL u) {
                    return new URLConnection(u) {
                        @Override
                        public void connect() {
                        }

                        @Override
                        public InputStream getInputStream() {
                            return new ByteArrayInputStream(data);
                        }
                    };
                }
            });
        }
        return url;

    }

    @Override
    public Enumeration<URL> findResources(String name, boolean check) {
        return new StaticURLs(delegate.findResources(name, check));
    }

    @Override
    public Enumeration<Resource> getResources(String name) {
        return new StaticResources(delegate.getResources(name));
    }

    public static class StaticURLs implements Enumeration<URL> {
        private final Enumeration<URL> delegate;

        public StaticURLs(Enumeration<URL> delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean hasMoreElements() {
            return delegate.hasMoreElements();
        }

        @Override
        public URL nextElement() {
            URL url = delegate.nextElement();
            String path = url.getPath();
            if (path.endsWith(".class")) {
                try {
                    return processURL(url, path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return url;
        }
    }

    public static class StaticResources implements Enumeration<Resource> {

        private final Enumeration<Resource> delegate;

        public StaticResources(Enumeration<Resource> delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean hasMoreElements() {
            return delegate.hasMoreElements();
        }

        @Override
        public Resource nextElement() {
            try {
                return processResource(delegate.nextElement(), delegate.nextElement().getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class StaticResource extends Resource {
        private final String _name;
        private final URL _url;
        private final URL _cs_url;
        private final InputStream is;
        private final int length;

        public StaticResource(String _name, URL _url, URL _cs_url, InputStream is, int length) {
            this._name = _name;
            this._url = _url;
            this._cs_url = _cs_url;
            this.is = is;
            this.length = length;
        }

        @Override
        public String getName() {
            return _name;
        }

        @Override
        public URL getURL() {
            return _url;
        }

        @Override
        public URL getCodeSourceURL() {
            return _cs_url;
        }

        @Override
        public InputStream getInputStream() {
            return is;
        }

        @Override
        public int getContentLength() {
            return length;
        }
    }
}
