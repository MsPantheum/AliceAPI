package alice.util;

import javax.swing.*;
import java.lang.management.ManagementFactory;
import java.util.*;

public class ProcessUtil {
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

}
