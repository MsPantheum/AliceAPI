package alice.test;

import alice.injector.ClassPatcher;
import alice.util.DebugUtil;
import org.junit.jupiter.api.Test;
import sun.jvm.hotspot.HSDB;
import sun.jvm.hotspot.HotSpotAgent;
import sun.jvm.hotspot.types.Type;
import sun.jvm.hotspot.utilities.WorkerThread;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.fail;

public class TestHSDB {
    @Test
    @SuppressWarnings("unchecked")
    public void test(){
        ClassPatcher.load();
        try {
            Constructor<HSDB> constructor = HSDB.class.getDeclaredConstructor(String[].class);
            constructor.setAccessible(true);
            HSDB hsdb = constructor.newInstance((Object) new String[0]);
            Field f = HSDB.class.getDeclaredField("agent");
            f.setAccessible(true);
            HotSpotAgent agent = new HotSpotAgent();
            f.set(hsdb,agent);
            f = HSDB.class.getDeclaredField("workerThread");
            f.setAccessible(true);
            f.set(hsdb,new WorkerThread());
            agent.attach(0);
            Type type = agent.getTypeDataBase().lookupType("InstanceKlass");
            DebugUtil.printType(type);
        } catch (Throwable e) {
            fail(e);
        }
    }
}