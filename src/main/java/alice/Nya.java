package alice;

import sun.jvm.hotspot.HSDB;
import sun.jvm.hotspot.HotSpotAgent;

public class Nya {
    public static void main(String[] args) {
        System.out.println(HSDB.class.getName());
        System.out.println(HotSpotAgent.class.getName());
    }
}
