package alice.exception;

public class ExitNow extends RuntimeException {
    public ExitNow(String message) {
        super(message);
    }

    public ExitNow(Throwable e) {
        super(e);
    }
}
