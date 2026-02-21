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

    private final URLClassPath ucp;

    private static final List<ClassByteProcessor> PROCESSORS = new LinkedList<>();

    public static void registerProcessor(ClassByteProcessor processor) {
        PROCESSORS.add(processor);
    }

    public URLClassPathWrapper(URLClassPath ucp){
        super(ucp.getURLs());
        this.ucp = ucp;
    }

    @Override
    public Resource getResource(String name, boolean check) {
        Resource ret = ucp.getResource(name, check);
        if(ret != null){
            if(!PROCESSORS.isEmpty()){
                try {
                    byte[] data = ret.getBytes();
                    for(ClassByteProcessor processor : PROCESSORS){
                        data = processor.process(name,data);
                    }
                    InputStream is =  new ByteArrayInputStream(data);
                    String _name = ret.getName();
                    URL _url = ret.getURL();
                    URL _cs_url = ret.getCodeSourceURL();
                    int length = data.length;
                    return new Resource() {
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
                    };
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return ret;
    }

    @Override
    public synchronized void addURL(URL url) {
        ucp.addURL(url);
    }

    @Override
    public synchronized List<IOException> closeLoaders() {
        return ucp.closeLoaders();
    }

    @Override
    public URL[] getURLs() {
        return ucp.getURLs();
    }

    @Override
    public Enumeration<Resource> getResources(String name, boolean check) {
        return ucp.getResources(name, check);
    }

    @Override
    public URL checkURL(URL url) {
        return ucp.checkURL(url);
    }
}
