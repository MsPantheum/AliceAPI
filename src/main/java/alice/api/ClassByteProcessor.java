package alice.api;

/**
 * A class byte processor.
 */
public interface ClassByteProcessor {

    /**
     * Method to process raw class bytes.
     *
     * @param classBytes class bytes to be processed, which may be null
     * @param name       class name, which will in format like java/lang/Class.class
     * @return processed class bytes
     */
    default byte[] process(byte[] classBytes, String name) {
        if (classBytes == null) {
            return null;
        }
        return processChecked(classBytes, name);
    }

    /**
     * Method to process raw class bytes. Input bytes are nonnull.<br>You should always override this method except that your transformer expects null input.
     *
     * @param classBytes class bytes to be processed, which is nonnull.
     * @param name       class name, which will in format like java/lang/Class.class
     * @return processed class bytes
     */
    default byte[] processChecked(byte[] classBytes, String name) {
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
