package alice.util;

import alice.Platform;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.module.ModuleDescriptor;
import java.util.HashSet;
import java.util.Set;

public class ModuleUtil {

    private static final VarHandle ModuleDescriptor_open;
    private static final VarHandle ModuleDescriptor_automatic;
    private static final VarHandle ModuleDescriptor_modifiers;

    static {
        if (!Platform.module) {
            throw new IllegalStateException();
        }
        MethodHandles.Lookup lookup = ReflectionUtil.lookup();
        try {
            ModuleDescriptor_open = lookup.findVarHandle(ModuleDescriptor.class, "open", boolean.class);
            ModuleDescriptor_automatic = lookup.findVarHandle(ModuleDescriptor.class, "automatic", boolean.class);
            ModuleDescriptor_modifiers = lookup.findVarHandle(ModuleDescriptor.class, "modifiers", Set.class);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void open(Module module) {
        ModuleDescriptor md = module.getDescriptor();
        ModuleDescriptor_open.set(md, true);
        ModuleDescriptor_automatic.set(md, true);
        Set<ModuleDescriptor.Modifier> modifiers = new HashSet<>();
        modifiers.add(ModuleDescriptor.Modifier.AUTOMATIC);
        modifiers.add(ModuleDescriptor.Modifier.OPEN);
        ModuleDescriptor_modifiers.set(md, modifiers);
    }
}
