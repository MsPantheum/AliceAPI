package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiHeapReferenceInfoStackLocal extends NativeObject {

    public static final long SIZE = 48L;

    private static final long thread_tag_offset = 0L;
    private static final long thread_id_offset = 8L;
    private static final long depth_offset = 16L;
    private static final long method_offset = 24L;
    private static final long location_offset = 32L;
    private static final long slot_offset = 40L;

    public jvmtiHeapReferenceInfoStackLocal(long address) {
        super(address);
    }

    public jvmtiHeapReferenceInfoStackLocal() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long thread_tag() {
        return Unsafe.getLong(address + thread_tag_offset);
    }

    public void thread_tag(long thread_tag) {
        Unsafe.putLong(address + thread_tag_offset, thread_tag);
    }

    public long thread_id() {
        return Unsafe.getLong(address + thread_id_offset);
    }

    public void thread_id(long thread_id) {
        Unsafe.putLong(address + thread_id_offset, thread_id);
    }

    public int depth() {
        return Unsafe.getInt(address + depth_offset);
    }

    public void depth(int depth) {
        Unsafe.putInt(address + depth_offset, depth);
    }

    public long method() {
        return Unsafe.getLong(address + method_offset);
    }

    public void method(long method) {
        Unsafe.putLong(address + method_offset, method);
    }

    public long location() {
        return Unsafe.getLong(address + location_offset);
    }

    public void location(long location) {
        Unsafe.putLong(address + location_offset, location);
    }

    public int slot() {
        return Unsafe.getInt(address + slot_offset);
    }

    public void slot(int slot) {
        Unsafe.putInt(address + slot_offset, slot);
    }
}
