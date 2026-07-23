package alice.exception;

public class NoSuchFunctionException extends RuntimeException {
    public NoSuchFunctionException(String name) {
        super("Function ".concat(name).concat(" not found!"));
    }
}
