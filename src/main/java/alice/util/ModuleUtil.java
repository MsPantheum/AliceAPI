package alice.util;

import alice.Platform;

public class ModuleUtil {
    static {
        if (!Platform.module) {
            throw new IllegalStateException();
        }
    }

    public static void open(Module module) {

    }
}
