package alice.util;

import alice.api.ClassByteProcessor;
import sun.misc.Resource;
import sun.misc.URLClassPath;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class URLClassPathWrapper extends URLClassPath {

    private final URLClassPath delegate;

    private static final List<ClassByteProcessor> PROCESSORS = new LinkedList<>();

    public static void registerProcessor(ClassByteProcessor processor) {
        PROCESSORS.add(processor);
    }

    public URLClassPathWrapper(URLClassPath delegate) {
        super(delegate.getURLs());
        this.delegate = delegate;
    }

    @Override
    public Resource getResource(String name, boolean check) {
        Resource ret = delegate.getResource(name, check);
        if(ret != null){
            if(!PROCESSORS.isEmpty()){
                try {
                    byte[] data = ret.getBytes();
                    for(ClassByteProcessor processor : PROCESSORS){
                        data = processor.process(data, name);
                    }
                    InputStream is =  new ByteArrayInputStream(data);
                    String _name = ret.getName();
                    URL _url = ret.getURL();
                    URL _cs_url = ret.getCodeSourceURL();
                    int length = data.length;
                    return new StaticResource(_name, _url, _cs_url, is, length);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return ret;
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
        return delegate.getURLs();
    }

    @Override
    public Enumeration<Resource> getResources(String name, boolean check) {
        return delegate.getResources(name, check);
    }

    @Override
    public URL checkURL(URL url) {
        return delegate.checkURL(url);
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
