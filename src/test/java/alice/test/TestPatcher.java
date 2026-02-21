package alice.test;

import alice.injector.patch.PatcherLoader;
import alice.util.Unsafe;
import org.junit.jupiter.api.Test;
import sun.jvm.hotspot.debugger.MachineDescriptionAMD64;
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
        LinuxDebuggerLocal test = new LinuxDebuggerLocal(new MachineDescriptionAMD64(),false);
                //Unsafe.allocateInstance(LinuxDebuggerLocal.class);
        try {
//            Method m = LinuxDebuggerLocal.class.getDeclaredMethod("init0");
//            m.setAccessible(true);
//            m.invoke(test);
//            m = LinuxDebuggerLocal.class.getDeclaredMethod("attach0", int.class);
//            m.setAccessible(true);
//            m.invoke(test,0);
//            m = LinuxDebuggerLocal.class.getDeclaredMethod("lookupByName0", String.class, String.class);
//            m.setAccessible(true);
//            System.out.println(m.invoke(test,"libjvm.so", "__vt_10JavaThread"));
            System.out.println("Try to attach.");
            test.attach(0);
            System.out.println(test.lookup("libjvm.so","gHotSpotVMTypes"));
            System.out.println("End.");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        System.out.println("End.");
    }
}
