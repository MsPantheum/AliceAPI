package java.lang.module;

import java.util.Set;

public interface ModuleFinder {
    static ModuleFinder ofSystem() {
        return null;
    }

    Set<ModuleReference> findAll();
}
