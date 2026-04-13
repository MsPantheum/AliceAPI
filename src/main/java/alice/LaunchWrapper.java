package alice;

import alice.util.DebugUtil;
import alice.util.ReflectionUtil;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class LaunchWrapper {

    public static void main(String[] args) {
        Init.ensureInit();
        ExtensionLoader.load();
        try {
            String target_name = System.getProperty("alice.launch_target");
            if (target_name == null) {
                target_name = "net.minecraft.launchwrapper.Launch";
            }
            Class<?> launch_target = Class.forName(target_name);
            MethodHandles.Lookup lookup = ReflectionUtil.lookup();
            MethodHandle main = lookup.findStatic(launch_target, "main", MethodType.methodType(void.class, String[].class));
            main.invoke((String[]) args);
        } catch (Throwable t){
            DebugUtil.printThrowableFully(t);
        }
    }
}
