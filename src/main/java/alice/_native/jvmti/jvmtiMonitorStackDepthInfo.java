package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiMonitorStackDepthInfo extends NativeObject {

    public static final long SIZE = 16L;

    private static final long monitor_offset = 0L;
    private static final long stack_depth_offset = 8L;

    public jvmtiMonitorStackDepthInfo(long address) {
        super(address);
    }

    public jvmtiMonitorStackDepthInfo() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long monitor() {
        return Unsafe.getLong(address + monitor_offset);
    }

    public void monitor(long monitor) {
        Unsafe.putLong(address + monitor_offset, monitor);
    }

    public int stack_depth() {
        return Unsafe.getInt(address + stack_depth_offset);
    }

    public void stack_depth(int stack_depth) {
        Unsafe.putInt(address + stack_depth_offset, stack_depth);
    }
}
