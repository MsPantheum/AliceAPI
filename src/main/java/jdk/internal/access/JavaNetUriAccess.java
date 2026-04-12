package jdk.internal.access;

import java.net.URI;

public interface JavaNetUriAccess {
    URI create(String scheme, String path);
}
