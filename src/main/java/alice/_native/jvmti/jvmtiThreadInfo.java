package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiThreadInfo extends NativeObject {

    public static final long SIZE = 32L;

    private static final long name_offset = 0L;
    private static final long priority_offset = 8L;
    private static final long is_daemon_offset = 12L;
    private static final long thread_group_offset = 16L;
    private static final long context_class_loader_offset = 24L;

    public jvmtiThreadInfo(long address) {
        super(address);
    }

    public jvmtiThreadInfo() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long name() {
        return Unsafe.getLong(address + name_offset);
    }

    public void name(long name) {
        Unsafe.putLong(address + name_offset, name);
    }

    public int priority() {
        return Unsafe.getInt(address + priority_offset);
    }

    public void priority(int priority) {
        Unsafe.putInt(address + priority_offset, priority);
    }

    public boolean is_daemon() {
        return Unsafe.getByte(address + is_daemon_offset) != 0;
    }

    public void is_daemon(boolean is_daemon) {
        Unsafe.putByte(address + is_daemon_offset, (byte) (is_daemon ? 1 : 0));
    }

    public long thread_group() {
        return Unsafe.getLong(address + thread_group_offset);
    }

    public void thread_group(long thread_group) {
        Unsafe.putLong(address + thread_group_offset, thread_group);
    }

    public long context_class_loader() {
        return Unsafe.getLong(address + context_class_loader_offset);
    }

    public void context_class_loader(long context_class_loader) {
        Unsafe.putLong(address + context_class_loader_offset, context_class_loader);
    }
}
