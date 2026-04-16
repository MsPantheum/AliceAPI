package alice;

import alice.exception.ExitNow;
import alice.log.Logger;
import alice.util.DebugUtil;
import alice.util.ReflectionUtil;
import alice.util.Unsafe;
import sun.jvm.hotspot.HotSpotAgent;
import sun.jvm.hotspot.debugger.Debugger;
import sun.jvm.hotspot.debugger.JVMDebugger;
import sun.jvm.hotspot.types.TypeDataBase;

import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;

public final class HSDB {
    public static final HotSpotAgent agent;
    public static final TypeDataBase typeDataBase;
    public static final Debugger debugger;

    static {
        System.setProperty("sun.jvm.hotspot.runtime.VM.disableVersionCheck", "true");
        try {
            Logger.MAIN.debug("hotspot agent's class is loaded by: " + sun.jvm.hotspot.HSDB.class.getClassLoader());
            sun.jvm.hotspot.HSDB HSDB = Unsafe.allocateInstance(sun.jvm.hotspot.HSDB.class);
            agent = new HotSpotAgent();
            if (Platform.jigsaw) {
                VarHandle vh = ReflectionUtil.findVarHandle(sun.jvm.hotspot.HSDB.class, "agent", HotSpotAgent.class);
                vh.set(HSDB, agent);
            } else {
                Field f = ReflectionUtil.getField(sun.jvm.hotspot.HSDB.class, "agent");
                Unsafe.putObject(HSDB, Unsafe.objectFieldOffset(f), agent);
            }
            agent.attach(0);
            if (Platform.jigsaw) {
                VarHandle vh = ReflectionUtil.findVarHandle(HotSpotAgent.class, "debugger", JVMDebugger.class);
                debugger = (Debugger) vh.get(agent);
            } else {
                Field f = ReflectionUtil.getField(HotSpotAgent.class, "debugger");
                debugger = Unsafe.getObject(agent, Unsafe.objectFieldOffset(f));
            }
            typeDataBase = agent.getTypeDataBase();
        } catch (Throwable e) {
            DebugUtil.printThrowableFully(e);
            throw new ExitNow(e);
        }
    }
}
