package java.lang.module;

import java.io.IOException;

public abstract class ModuleReference {
    public final ModuleDescriptor descriptor() {
        return null;
    }

    public abstract ModuleReader open() throws IOException;
}
