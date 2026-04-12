package alice;

import alice.exception.ExitNow;
import alice.util.DebugUtil;
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
        System.setProperty("sun.jvm.hotspot.runtime.VM.disableVersionCheck", "true");
        System.out.println(alice.HSDB.class.getClassLoader().getClass().getName());
        try {
            HSDB = Unsafe.allocateInstance(sun.jvm.hotspot.HSDB.class);
            Field f = sun.jvm.hotspot.HSDB.class.getDeclaredField("agent");
            agent = new HotSpotAgent();
            Unsafe.putObject(HSDB, Unsafe.objectFieldOffset(f), agent);
            agent.attach(0);
            f = HotSpotAgent.class.getDeclaredField("debugger");
            debugger = Unsafe.getObject(agent, Unsafe.objectFieldOffset(f));
            typeDataBase = agent.getTypeDataBase();
        } catch (Throwable e) {
            DebugUtil.printThrowableFully(e);
            throw new ExitNow(e);
        }
    }
}
