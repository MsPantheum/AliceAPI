package alice.injector.classloading;

import alice.Meow;
import alice.injector.ClassPatcher;
import alice.util.FileUtil;
import sun.misc.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.BiFunction;

@SuppressWarnings("DuplicatedCode")
public class ResourceWrapper {

    private static final String ALICE_JAR = FileUtil.getJarPath(Meow.class);

    public static class LegacyResource {

        public static BiFunction<Resource, String, Resource> legacyResourceFunction = (resource, name) -> {
            try {
                return processLegacyResource(resource, name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        public static Resource processLegacyResource(Resource resource, String name) throws IOException {
            boolean flag = resource != null;
            if (ClassPatcher.shouldRunTransformers()) {
                byte[] data = ClassPatcher.runTransformers(resource != null ? resource.getBytes() : null, name);
                if (data == null) {
                    return resource;
                }
                InputStream is = new ByteArrayInputStream(data);
                String _name = flag ? resource.getName() : name;
                URL _url = flag ? resource.getURL() : null;
                URL _cs_url = flag ? resource.getCodeSourceURL() : new URL("file:".concat(ALICE_JAR));
                int length = data.length;
                return new LegacyStaticResource(_name, _url, _cs_url, is, length);
            }
            return resource;
        }

        public static class LegacyStaticResource extends Resource {
            private final String _name;
            private final URL _url;
            private final URL _cs_url;
            private final InputStream is;
            private final int length;

            public LegacyStaticResource(String _name, URL _url, URL _cs_url, InputStream is, int length) {
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

    public static class InternalResource {

        public static BiFunction<jdk.internal.loader.Resource, String, jdk.internal.loader.Resource> resourceFunction = (resource, name) -> {
            try {
                return processResource(resource, name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        public static jdk.internal.loader.Resource processResource(jdk.internal.loader.Resource resource, String name) throws IOException {
            boolean flag = resource != null;
            if (ClassPatcher.shouldRunTransformers()) {
                byte[] data = ClassPatcher.runTransformers(flag ? resource.getBytes() : null, name);
                if (data == null) {
                    return resource;
                }
                InputStream is = new ByteArrayInputStream(data);
                String _name = flag ? resource.getName() : name;
                URL _url = flag ? resource.getURL() : null;
                URL _cs_url = flag ? resource.getCodeSourceURL() : new URL("file:".concat(ALICE_JAR));
                int length = data.length;
                return new StaticResource(_name, _url, _cs_url, is, length);
            }
            return resource;
        }

        public static class StaticResource extends jdk.internal.loader.Resource {
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

}
