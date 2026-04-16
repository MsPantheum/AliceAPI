package java.lang;

import java.util.Set;

public class ModuleLayer {
    public static ModuleLayer boot() {
        return null;
    }

    public Set<Module> modules() {
        return null;
    }

    public static final class Controller {
        private ModuleLayer layer;

        public ModuleLayer layer() {
            return layer;
        }

    }
}
