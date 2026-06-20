package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiLineNumberEntry extends NativeObject {

    public static final long SIZE = 16L;

    private static final long start_location_offset = 0L;
    private static final long line_number_offset = 8L;

    public jvmtiLineNumberEntry(long address) {
        super(address);
    }

    public jvmtiLineNumberEntry() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long start_location() {
        return Unsafe.getLong(address + start_location_offset);
    }

    public void start_location(long start_location) {
        Unsafe.putLong(address + start_location_offset, start_location);
    }

    public int line_number() {
        return Unsafe.getInt(address + line_number_offset);
    }

    public void line_number(int line_number) {
        Unsafe.putInt(address + line_number_offset, line_number);
    }
}
