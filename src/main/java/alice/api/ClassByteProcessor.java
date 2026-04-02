package alice.api;

public interface ClassByteProcessor {

    //name is in format like java/lang/Class.class
    default byte[] process(byte[] classBytes, String name) {
        return classBytes;
    }

    default int priority() {
        return 0;
    }
}
