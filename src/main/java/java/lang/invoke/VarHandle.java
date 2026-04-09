package java.lang.invoke;

public class VarHandle {
    public native void set(Object... args);

    public native Object get(Object... args);
}
