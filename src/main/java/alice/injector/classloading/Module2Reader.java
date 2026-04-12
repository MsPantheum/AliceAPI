package alice.injector.classloading;

import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Module2Reader extends ConcurrentHashMap<ModuleReference, ModuleReader> {

    public Module2Reader(Map<ModuleReference, ModuleReader> orig) {
        orig.forEach((ref, reader) -> super.put(ref, new WrappedModuleReader(reader)));
    }

    @Override
    public ModuleReader put(ModuleReference key, ModuleReader value) {
        return super.put(key, new WrappedModuleReader(value));
    }

    @Override
    public ModuleReader computeIfAbsent(ModuleReference key, Function<? super ModuleReference, ? extends ModuleReader> mappingFunction) {
        Function<ModuleReference, ModuleReader> create = moduleReference -> new WrappedModuleReader(mappingFunction.apply(moduleReference));
        return super.computeIfAbsent(key, create);
    }
}
