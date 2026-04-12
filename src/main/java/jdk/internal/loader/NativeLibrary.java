package jdk.internal.loader;

public interface NativeLibrary {
    String name();

    long find(String name);
}
