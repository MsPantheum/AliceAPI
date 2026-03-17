package alice;

import alice.util.Unsafe;
import sun.jvm.hotspot.HotSpotAgent;
import sun.jvm.hotspot.debugger.Debugger;
import sun.jvm.hotspot.types.TypeDataBase;

import java.lang.reflect.Field;

public class HSDB {
    private static final sun.jvm.hotspot.HSDB HSDB;
    public static final HotSpotAgent agent;
    public static final TypeDataBase typeDataBase;
    public static final Debugger debugger;

    static {
        Init.ensureInit();
        HSDB = Unsafe.allocateInstance(sun.jvm.hotspot.HSDB.class);
        try {
            Field f = sun.jvm.hotspot.HSDB.class.getDeclaredField("agent");
            agent = new HotSpotAgent();
            Unsafe.putObject(HSDB,Unsafe.objectFieldOffset(f),agent);
            agent.attach(0);
            debugger = agent.getDebugger();
            typeDataBase = agent.getTypeDataBase();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
