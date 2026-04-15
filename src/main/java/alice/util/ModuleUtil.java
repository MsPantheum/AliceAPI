package alice.util;

import alice.Platform;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.util.*;

@SuppressWarnings("unchecked")
public class ModuleUtil {

    private static final VarHandle ModuleDescriptor_open;
    private static final VarHandle ModuleDescriptor_automatic;
    private static final VarHandle ModuleDescriptor_modifiers;
    private static final VarHandle ModuleDescriptor_openPackages;

    private static final MethodHandle defineModule0;
    private static final MethodHandle addReads0;
    private static final MethodHandle addExports0;
    private static final MethodHandle addExportsToAll0;
    private static final MethodHandle addExportsToAllUnnamed0;

    private static final Module ALL_UNNAMED_MODULE;
    private static final Set<Module> ALL_UNNAMED_MODULE_SET;

    private static final Module EVERYONE_MODULE;
    private static final Set<Module> EVERYONE_SET;

    static {
        if (!Platform.jigsaw) {
            throw new IllegalStateException();
        }
        ModuleDescriptor_open = ReflectionUtil.findVarHandle(ModuleDescriptor.class, "open", boolean.class);
        ModuleDescriptor_automatic = ReflectionUtil.findVarHandle(ModuleDescriptor.class, "automatic", boolean.class);
        ModuleDescriptor_modifiers = ReflectionUtil.findVarHandle(ModuleDescriptor.class, "modifiers", Set.class);
        ModuleDescriptor_openPackages = ReflectionUtil.findVarHandle(Module.class, "openPackages", Map.class);
        defineModule0 = ReflectionUtil.findStatic(Module.class, "defineModule0", MethodType.methodType(void.class, Module.class, boolean.class, String.class, String.class, Object[].class));
        addReads0 = ReflectionUtil.findStatic(Module.class, "addReads0", MethodType.methodType(void.class, Module.class, Module.class));
        addExports0 = ReflectionUtil.findStatic(Module.class, "addExports0", MethodType.methodType(void.class, Module.class, String.class, Module.class));
        addExportsToAll0 = ReflectionUtil.findStatic(Module.class, "addExportsToAll0", MethodType.methodType(void.class, Module.class, String.class));
        addExportsToAllUnnamed0 = ReflectionUtil.findStatic(Module.class, "addExportsToAllUnnamed0", MethodType.methodType(void.class, Module.class, String.class));
        ALL_UNNAMED_MODULE = (Module) ReflectionUtil.findStaticVarHandle(Module.class, "ALL_UNNAMED_MODULE", Module.class).get();
        ALL_UNNAMED_MODULE_SET = (Set<Module>) ReflectionUtil.findStaticVarHandle(Module.class, "ALL_UNNAMED_MODULE_SET", Set.class).get();
        EVERYONE_MODULE = (Module) ReflectionUtil.findStaticVarHandle(Module.class, "EVERYONE_MODULE", Module.class).get();
        EVERYONE_SET = (Set<Module>) ReflectionUtil.findStaticVarHandle(Module.class, "EVERYONE_SET", Set.class).get();

    }

    public static void addReads(Module from, Module to) {
        try {
            addReads0.invoke(from, to);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void addExports(Module from, String pn, Module to) {
        try {
            addExports0.invoke(from, pn, to);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void addExportsToAll(Module from, String pn) {
        try {
            addExportsToAll0.invoke(from, pn);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void addExportsToAllUnnamed(Module from, String pn) {
        try {
            addExportsToAllUnnamed0.invoke(from, pn);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void open(Module module) {
        open(module.getDescriptor());
        ModuleDescriptor_openPackages.set(module, LetMeAccessYouIdiot.INSTANCE);
        for (String p : module.getPackages()) {
            addExportsToAll(module, p);
            addExportsToAllUnnamed(module, p);
        }
    }

    public static void open(ModuleDescriptor md) {
        ModuleDescriptor_open.set(md, true);
        ModuleDescriptor_automatic.set(md, true);
        Set<ModuleDescriptor.Modifier> modifiers = new HashSet<>();
        modifiers.add(ModuleDescriptor.Modifier.AUTOMATIC);
        modifiers.add(ModuleDescriptor.Modifier.OPEN);
        ModuleDescriptor_modifiers.set(md, modifiers);
    }

    public static void openAll() {
        for (Module module : ModuleLayer.boot().modules()) {
            open(module);
        }
        for (ModuleReference reference : ModuleFinder.ofSystem().findAll()) {
            open(reference.descriptor());
        }
        Unsafe.enableInternalUnsafe();
    }

    private static class LetMeAccessYouIdiot extends AbstractMap<String, Set<Module>> {

        private static final LetMeAccessYouIdiot INSTANCE = new LetMeAccessYouIdiot();

        private LetMeAccessYouIdiot() {
        }

        @Override
        public Set<Module> get(Object key) {
            return EVERYONE_SET;
        }

        @Override
        public Set<Entry<String, Set<Module>>> entrySet() {
            return Collections.emptySet();
        }
    }
}
