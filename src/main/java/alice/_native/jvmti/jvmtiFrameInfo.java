package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiFrameInfo extends NativeObject {

    public static final long SIZE = 16L;

    private static final long method_offset = 0L;
    private static final long location_offset = 8L;

    public jvmtiFrameInfo(long address) {
        super(address);
    }

    public jvmtiFrameInfo() {
    }

    @Override
    public long getSize() {
        return SIZE;
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
}
