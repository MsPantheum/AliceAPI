package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiThreadGroupInfo extends NativeObject {

    public static final long SIZE = 24L;

    private static final long parent_offset = 0L;
    private static final long name_offset = 8L;
    private static final long max_priority_offset = 16L;
    private static final long is_daemon_offset = 20L;

    public jvmtiThreadGroupInfo(long address) {
        super(address);
    }

    public jvmtiThreadGroupInfo() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long parent() {
        return Unsafe.getLong(address + parent_offset);
    }

    public void parent(long parent) {
        Unsafe.putLong(address + parent_offset, parent);
    }

    public long name() {
        return Unsafe.getLong(address + name_offset);
    }

    public void name(long name) {
        Unsafe.putLong(address + name_offset, name);
    }

    public int max_priority() {
        return Unsafe.getInt(address + max_priority_offset);
    }

    public void max_priority(int max_priority) {
        Unsafe.putInt(address + max_priority_offset, max_priority);
    }

    public boolean is_daemon() {
        return Unsafe.getByte(address + is_daemon_offset) != 0;
    }

    public void is_daemon(boolean is_daemon) {
        Unsafe.putByte(address + is_daemon_offset, (byte) (is_daemon ? 1 : 0));
    }
}
