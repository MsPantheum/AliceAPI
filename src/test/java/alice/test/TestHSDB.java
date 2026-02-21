package alice.test;

import alice.injector.patch.PatcherLoader;
import org.junit.jupiter.api.Test;
import sun.jvm.hotspot.HSDB;
import sun.jvm.hotspot.HotSpotAgent;
import sun.jvm.hotspot.types.Type;
import sun.jvm.hotspot.utilities.WorkerThread;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

public class TestHSDB {
    @Test
    public void test(){
        PatcherLoader.load();
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
            System.out.println(type.getSize());
            Iterator<sun.jvm.hotspot.types.Field> iterator = type.getFields();
            iterator.forEachRemaining(field -> {
                System.out.println(field.getName());
                System.out.println(field.getSize());
                System.out.println(field.getOffset());
            });
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}