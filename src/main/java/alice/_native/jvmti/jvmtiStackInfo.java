package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiStackInfo extends NativeObject {

    public static final long SIZE = 32L;

    private static final long thread_offset = 0L;
    private static final long state_offset = 8L;
    private static final long frame_buffer_offset = 16L;
    private static final long frame_count_offset = 24L;

    public jvmtiStackInfo(long address) {
        super(address);
    }

    public jvmtiStackInfo() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long thread() {
        return Unsafe.getLong(address + thread_offset);
    }

    public void thread(long thread) {
        Unsafe.putLong(address + thread_offset, thread);
    }

    public int state() {
        return Unsafe.getInt(address + state_offset);
    }

    public void state(int state) {
        Unsafe.putInt(address + state_offset, state);
    }

    public long frame_buffer() {
        return Unsafe.getLong(address + frame_buffer_offset);
    }

    public void frame_buffer(long frame_buffer) {
        Unsafe.putLong(address + frame_buffer_offset, frame_buffer);
    }

    public int frame_count() {
        return Unsafe.getInt(address + frame_count_offset);
    }

    public void frame_count(int frame_count) {
        Unsafe.putInt(address + frame_count_offset, frame_count);
    }
}
