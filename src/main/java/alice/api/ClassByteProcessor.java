package alice.api;

/**
 * A class byte processor.
 */
public interface ClassByteProcessor {

    /**
     * Method to process raw class bytes.
     *
     * @param classBytes class bytes to be processed.
     * @param name       class name, will in format like java/lang/Class.class.
     * @return processed class bytes, do not return null.
     */
    default byte[] process(byte[] classBytes, String name) {
        return classBytes;
    }

    /**
     * The priority of this processor.
     *
     * @return the priority, lower priority means the processor will be invoked earlier.
     */
    default int priority() {
        return 0;
    }

    /**
     * Indicates whether this processor shall be removed.
     *
     * @return whether this processor shall be removed
     */
    default boolean endOfLife() {
        return false;
    }
}
