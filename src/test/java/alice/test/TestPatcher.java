package alice.test;

import alice.injector.patch.PatcherLoader;
import alice.util.Unsafe;
import org.junit.jupiter.api.Test;
import sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestPatcher {
    @Test
    public void test(){
        System.out.println("Start loading...");
        PatcherLoader.load();
        System.out.println("Loading completed.");
        try {
            ClassLoader.getSystemClassLoader().loadClass("sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            if(e.getCause() != null){
                e.getCause().printStackTrace();
            }
            throw new RuntimeException(e);
        }
        LinuxDebuggerLocal test = Unsafe.allocateInstance(LinuxDebuggerLocal.class);
        try {
            Method m = LinuxDebuggerLocal.class.getDeclaredMethod("init0");
            m.setAccessible(true);
            m.invoke(test);
            m = LinuxDebuggerLocal.class.getDeclaredMethod("attach0", int.class);
            m.setAccessible(true);
            m.invoke(test,0);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        System.out.println("End.");
    }
}
