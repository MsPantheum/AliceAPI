package alice.exception;

public class NativeException extends RuntimeException {

    private final int error;

    public NativeException(String message, int error) {
        super(message);
        this.error = error;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + ", error code is ".concat(Integer.toString(error)).concat(".");
    }
}
