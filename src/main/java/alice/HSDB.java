package alice;

import alice.util.Unsafe;
import sun.jvm.hotspot.HotSpotAgent;
import sun.jvm.hotspot.types.TypeDataBase;
import sun.jvm.hotspot.utilities.WorkerThread;

import java.lang.reflect.Field;

public class HSDB {
    private static final sun.jvm.hotspot.HSDB HSDB;
    private static final HotSpotAgent agent;
    private static final TypeDataBase typeDataBase;

    static {
        HSDB = Unsafe.allocateInstance(sun.jvm.hotspot.HSDB.class);
        try {
            Field f = sun.jvm.hotspot.HSDB.class.getDeclaredField("workerThread");
            Unsafe.putObject(HSDB,Unsafe.objectFieldOffset(f),new WorkerThread());
            f = sun.jvm.hotspot.HSDB.class.getDeclaredField("agent");
            agent = new HotSpotAgent();
            Unsafe.putObject(HSDB,Unsafe.objectFieldOffset(f),agent);
            agent.attach(0);
            typeDataBase = agent.getTypeDataBase();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
