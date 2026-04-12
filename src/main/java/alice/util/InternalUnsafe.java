package alice.util;

import alice.Platform;

public class InternalUnsafe {
    static {
        if (!Platform.jigsaw) {
            throw new IllegalStateException();
        }
    }
}
