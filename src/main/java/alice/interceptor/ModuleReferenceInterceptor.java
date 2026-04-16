package alice.interceptor;

import alice.injector.classloading.WrappedModuleReader;

import java.io.IOException;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;

public class ModuleReferenceInterceptor {
    public static ModuleReader open(ModuleReference ref) throws IOException {
        ModuleReader reader = ref.open();
        return new WrappedModuleReader(reader);
    }
}
