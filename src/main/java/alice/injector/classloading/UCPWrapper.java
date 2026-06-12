package alice.injector.classloading;

import alice.injector.ClassPatcher;
import sun.misc.Resource;
import sun.misc.URLClassPath;

import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.util.Enumeration;
import java.util.List;

/**
 * @deprecated Reconstruct a new url or resource causes too much problem. Migrate to override JarLoader as it's much cleaner.
 */
@SuppressWarnings("DuplicatedCode")
@Deprecated
public class UCPWrapper {

    /**
     * @deprecated The ucp in BuiltinClassLoader is annotated @Stable, which means that replacing ucp field no longer works.<br>However, this class will be kept if someone else may want to use it.
     */
    @Deprecated
    public static class URLClassPathWrapper extends jdk.internal.loader.URLClassPath {
        private final jdk.internal.loader.URLClassPath delegate;

        public URLClassPathWrapper(jdk.internal.loader.URLClassPath delegate) {
            super(delegate.getURLs(), AccessController.getContext());
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
            } else if (obj instanceof jdk.internal.loader.URLClassPath) {
                return obj.equals(delegate);
            }
            return false;
        }

        @Override
        public URL findResource(String name, boolean check) throws IOException {
            return ClassPatcher.processURL(delegate.findResource(name, check), name);
        }

        @Override
        public jdk.internal.loader.Resource getResource(String name, boolean check) {
            try {
                return ResourceWrapper.InternalResource.processResource(delegate.getResource(name, check), name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Enumeration<URL> findResources(String name, boolean check) {
            return new StaticURLs(delegate.findResources(name, check));
        }

        @Override
        public Enumeration<jdk.internal.loader.Resource> getResources(String name, boolean check) {
            return new CollectionWrapper.StaticResources(delegate.getResources(name, check));
        }

        @Override
        public Enumeration<jdk.internal.loader.Resource> getResources(String name) {
            return new CollectionWrapper.StaticResources(delegate.getResources(name));
        }

        @Override
        public jdk.internal.loader.Resource getResource(String name) {
            return super.getResource(name);
        }

    }

    public static class LegacyURLClassPathWrapper extends URLClassPath {

        private final URLClassPath delegate;

        public LegacyURLClassPathWrapper(URLClassPath delegate) {
            super(delegate.getURLs());
            this.delegate = delegate;
        }

        @Override
        public int hashCode() {
            return delegate.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof LegacyURLClassPathWrapper) {
                return ((LegacyURLClassPathWrapper) obj).delegate.equals(delegate);
            } else if (obj instanceof URLClassPath) {
                return obj.equals(delegate);
            }
            return super.equals(obj);
        }

        @Override
        public Resource getResource(String name, boolean check) {
            try {
                return ResourceWrapper.LegacyResource.processLegacyResource(delegate.getResource(name, check), name);
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
                        ret[i] = ClassPatcher.processURL(url, path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return ret;
        }

        @Override
        public Enumeration<Resource> getResources(String name, boolean check) {
            return new CollectionWrapper.LegacyStaticResources(delegate.getResources(name, check));
        }

        @Override
        public URL checkURL(URL url) {
            return url;
        }

        @Override
        public URL findResource(String name, boolean check) {
            URL url = delegate.findResource(name, check);
            try {
                return ClassPatcher.processURL(url, name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Enumeration<URL> findResources(String name, boolean check) {
            return new StaticURLs(delegate.findResources(name, check));
        }

        @Override
        public Enumeration<Resource> getResources(String name) {
            return new CollectionWrapper.LegacyStaticResources(delegate.getResources(name));
        }

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
                    return ClassPatcher.processURL(url, path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return url;
        }
    }
}
