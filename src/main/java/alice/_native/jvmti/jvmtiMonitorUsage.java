package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiMonitorUsage extends NativeObject {

    public static final long SIZE = 40L;

    private static final long owner_offset = 0L;
    private static final long entry_count_offset = 8L;
    private static final long waiter_count_offset = 12L;
    private static final long waiters_offset = 16L;
    private static final long notify_waiter_count_offset = 24L;
    private static final long notify_waiters_offset = 32L;

    public jvmtiMonitorUsage(long address) {
        super(address);
    }

    public jvmtiMonitorUsage() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long owner() {
        return Unsafe.getLong(address + owner_offset);
    }

    public void owner(long owner) {
        Unsafe.putLong(address + owner_offset, owner);
    }

    public int entry_count() {
        return Unsafe.getInt(address + entry_count_offset);
    }

    public void entry_count(int entry_count) {
        Unsafe.putInt(address + entry_count_offset, entry_count);
    }

    public int waiter_count() {
        return Unsafe.getInt(address + waiter_count_offset);
    }

    public void waiter_count(int waiter_count) {
        Unsafe.putInt(address + waiter_count_offset, waiter_count);
    }

    public long waiters() {
        return Unsafe.getLong(address + waiters_offset);
    }

    public void waiters(long waiters) {
        Unsafe.putLong(address + waiters_offset, waiters);
    }

    public int notify_waiter_count() {
        return Unsafe.getInt(address + notify_waiter_count_offset);
    }

    public void notify_waiter_count(int notify_waiter_count) {
        Unsafe.putInt(address + notify_waiter_count_offset, notify_waiter_count);
    }

    public long notify_waiters() {
        return Unsafe.getLong(address + notify_waiters_offset);
    }

    public void notify_waiters(long notify_waiters) {
        Unsafe.putLong(address + notify_waiters_offset, notify_waiters);
    }
}
