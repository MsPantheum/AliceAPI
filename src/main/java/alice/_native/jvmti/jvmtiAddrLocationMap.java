package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiAddrLocationMap extends NativeObject {

    public static final long SIZE = 16L;

    private static final long start_address_offset = 0L;
    private static final long location_offset = 8L;

    public jvmtiAddrLocationMap(long address) {
        super(address);
    }

    public jvmtiAddrLocationMap() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long start_address() {
        return Unsafe.getLong(address + start_address_offset);
    }

    public void start_address(long start_address) {
        Unsafe.putLong(address + start_address_offset, start_address);
    }

    public long location() {
        return Unsafe.getLong(address + location_offset);
    }

    public void location(long location) {
        Unsafe.putLong(address + location_offset, location);
    }
}
