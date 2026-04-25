package alice.api;

/**
 * Provide class that is generated at runtime.
 */
public interface ClassProvider {

    /**
     * Provide a class.
     *
     * @param name the class which is required.
     * @return raw class bytes
     */
    byte[] provide(String name);

    /**
     * The priority of this provider.
     *
     * @return the priority, lower priority means the provider will be invoked earlier.
     */
    default int priority() {
        return 0;
    }

    /**
     * Indicates whether this provider shall be removed.
     *
     * @return whether this provider shall be removed
     */
    default boolean endOfLife() {
        return false;
    }
}
