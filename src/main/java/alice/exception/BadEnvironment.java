package alice.exception;

/**
 * You are using an unsupported environment or something is wrong with your environment.
 */
public class BadEnvironment extends Error {
    public BadEnvironment(String message) {
        super(message);
    }
}
