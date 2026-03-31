package alice;

import alice.util.ReflectionUtil;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

public class LaunchWrapper {
    public static void main(String[] args) throws Throwable {
        Init.ensureInit();
        Class<?> launch_target = Class.forName(System.getProperty("AliceLaunchTarget"));
        MethodHandle main = ReflectionUtil.findStatic(launch_target, "main", MethodType.methodType(void.class, String[].class));
        main.invoke((Object[]) args);
    }
}
