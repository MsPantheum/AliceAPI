package alice.util;

import javax.swing.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.management.ManagementFactory;
import java.util.Scanner;

public final class ProcessUtil {
    public static void pause(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("pause");
        while (scanner.hasNextLine()){
            scanner.nextLine();
        }
        scanner.close();
    }

    public static void guiPause(){
        JOptionPane.showMessageDialog(null, "Test paused. Click OK to continue.");
    }

    public static int getPID(){
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        return Integer.parseInt(pid);
    }

    private static final MethodHandle halt0;
    private static final MethodHandle beforeHalt;
    private static final MethodHandle runHooks;

    static {
        try {
            halt0 = ReflectionUtil.findStatic(Class.forName("java.lang.Shutdown"),"halt0", MethodType.methodType(void.class,int.class));
            beforeHalt = ReflectionUtil.findStatic(Class.forName("java.lang.Shutdown"),"beforeHalt", MethodType.methodType(void.class));
            runHooks = ReflectionUtil.findStatic(Class.forName("java.lang.Shutdown"),"runHooks", MethodType.methodType(void.class));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void exit(int status){
        try {
            if(status == 0){
                beforeHalt.invoke();
                runHooks.invoke();
            }
            halt0.invoke(status);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
