package alice.exception;

public class JNIException extends Error {

    private final int error;

    public JNIException(String message, int error) {
        super(message);
        this.error = error;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + ", error code is ".concat(Integer.toString(error)).concat(".");
    }
}
