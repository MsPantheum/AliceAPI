package alice.injector.classloading;

import sun.misc.Resource;

import java.io.IOException;
import java.util.Enumeration;

public class CollectionWrapper {
    public static class LegacyStaticResources implements Enumeration<Resource> {

        private final Enumeration<Resource> delegate;

        public LegacyStaticResources(Enumeration<Resource> delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean hasMoreElements() {
            return delegate.hasMoreElements();
        }

        @Override
        public Resource nextElement() {
            Resource res = delegate.nextElement();
            try {
                return ResourceWrapper.LegacyResource.processLegacyResource(res, res.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class StaticResources implements Enumeration<jdk.internal.loader.Resource> {

        private final Enumeration<jdk.internal.loader.Resource> delegate;

        public StaticResources(Enumeration<jdk.internal.loader.Resource> delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean hasMoreElements() {
            return delegate.hasMoreElements();
        }

        @Override
        public jdk.internal.loader.Resource nextElement() {
            jdk.internal.loader.Resource res = delegate.nextElement();
            try {
                return ResourceWrapper.InternalResource.processResource(res, res.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
