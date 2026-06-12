package alice.exception;

public class JNIException extends NativeException {

    public JNIException(String message, int error) {
        super(message, error);
    }

}
